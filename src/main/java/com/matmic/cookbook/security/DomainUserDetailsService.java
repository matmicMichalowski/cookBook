package com.matmic.cookbook.security;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptControlService loginAttemptControlService;


    private final HttpServletRequest request;

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }


    public DomainUserDetailsService(UserRepository userRepository, LoginAttemptControlService loginAttemptControlService) {
        this.userRepository = userRepository;
        this.loginAttemptControlService = loginAttemptControlService;
        this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
        .getRequest();
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        String ip = getIp();
        if (loginAttemptControlService.isBlocked(ip)){
            throw new LockedException("blocked");
        }

        String lowercaseUsername = userLogin.toLowerCase();
        Optional<User> findUser = userRepository.findOneWithAuthoritiesByName(lowercaseUsername);
        return findUser.map(user -> {
            if(!user.isActive()){
                throw new UserInactiveException("User with login " + lowercaseUsername + " is inactive.");
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseUsername, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("This username (" + lowercaseUsername + ") was not found in database"));
    }

    private String getIp(){
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }
}
