package org.taskntech.tech_flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                        .csrf(csrf -> csrf.disable())
                        .oauth2Login(oauth2 -> oauth2.disable())  // Add this line
                        .build();
        }
}