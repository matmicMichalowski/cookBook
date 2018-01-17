package com.matmic.cookbook.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;


@Configuration
public class WebConfiguration {

    private final Logger log = LoggerFactory.getLogger(WebConfiguration.class);


    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.setMaxAge(1800L);
        log.debug("Registering of CORS filter");
        src.registerCorsConfiguration("/api/**", config);
        src.registerCorsConfiguration("/api/register", config);
        return new CorsFilter(src);
    }

}
