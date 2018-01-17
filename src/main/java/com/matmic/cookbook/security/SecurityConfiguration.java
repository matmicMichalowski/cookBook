package com.matmic.cookbook.security;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final DomainUserDetailsService userDetailsService;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder,
                                 DomainUserDetailsService userDetailsService, CorsFilter corsFilter) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.corsFilter = corsFilter;
    }

    @PostConstruct
    public void init(){
        try{
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }catch(Exception ex){
            throw new BeanInitializationException("Security configuration failed", ex);
        }
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler();
    }

    @Bean
    public UnauthorizedEntryPointHttp401 unauthorizedEntryPointHttp401(){
        return new UnauthorizedEntryPointHttp401();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable().headers()
//        .frameOptions().disable();

        http
                .csrf().disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//       .and()
             .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
             .exceptionHandling()
             .authenticationEntryPoint(unauthorizedEntryPointHttp401())
       .and()
              .headers()
                .frameOptions()
                .disable()
       .and()
              .authorizeRequests()
              .antMatchers("/api/login").permitAll()
              .antMatchers("/api/ok").permitAll()
              .antMatchers("/api/activate").permitAll()
              .antMatchers("/api/register").permitAll()
              .antMatchers("/api/authenticate").permitAll()
              .antMatchers("/api/authentication").permitAll()
              .antMatchers("/api/registration").permitAll()
              .antMatchers("/api/reset-password").permitAll()
              .antMatchers("/api/reset").permitAll()
              .antMatchers("/400error").permitAll()
              .antMatchers(HttpMethod.GET, "/api/recipes").permitAll()
              .antMatchers(HttpMethod.GET, "/api/recipe/**").permitAll()
              .antMatchers(HttpMethod.POST, "/api/user").permitAll()
              .antMatchers("/api/ingredient**").fullyAuthenticated()
        .and()
              .formLogin()
              .loginProcessingUrl("/api/authentication")
              .successHandler(authenticationSuccessHandler())
              .failureHandler(authenticationFailureHandler())
              .usernameParameter("username")
              .passwordParameter("password")
              .permitAll()
        .and()
                .logout()
                .logoutUrl("/api/logout")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers( "/resources/**","/static/**" ,"/css/**", "/js/**", "/images/**", "/webjars/**", "/bootstrap/**");
    }
}
