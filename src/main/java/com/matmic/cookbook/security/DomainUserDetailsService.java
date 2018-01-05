package com.matmic.cookbook.security;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptControlService loginAttemptControlService;
    private HttpServletRequest request;

    public DomainUserDetailsService(UserRepository userRepository, LoginAttemptControlService loginAttemptControlService, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.loginAttemptControlService = loginAttemptControlService;
        this.request = request;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        String ip = getIp();
        if (loginAttemptControlService.isBlocked(ip)){
            throw new LockedException("blocked");
        }

        String lowcaseUsername = userLogin.toLowerCase();
        Optional<User> findUser = userRepository.findOneWithAuthoritiesByName(lowcaseUsername);
        return findUser.map(user -> {
            if(!user.isActive()){
                throw new UserInactiveException("User with login " + lowcaseUsername + " is inactive.");
            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowcaseUsername, user.getPassword(), grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("This username (" + lowcaseUsername + ") was not found in database"));
    }

    private String getIp(){
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }
}
