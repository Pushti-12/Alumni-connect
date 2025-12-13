package com.example.alumniconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Development security config: permit all requests and disable CSRF so API clients (curl/Postman/frontend)
     * can call the endpoints without authentication. REMOVE or tighten this for production.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())          // disable CSRF for APIs (only for dev)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()          // allow all requests
            )
            .httpBasic(Customizer.withDefaults()); // keep http basic available (optional)

        return http.build();
    }
}
