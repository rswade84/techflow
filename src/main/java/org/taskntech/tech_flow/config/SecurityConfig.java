package org.taskntech.tech_flow.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration

//this annotation exposes SecurityFilterChain bean & enables Spring Security integration with Spring MVC.
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll();
                    auth.requestMatchers("/", "/login", "/error", "/ws/**", "/topics/**", "/css/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(form -> form
                        .loginPage("/")
                        .defaultSuccessUrl("/tickets/create", true))
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )
                .build();
    }

}


