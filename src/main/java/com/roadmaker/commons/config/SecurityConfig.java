package com.roadmaker.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic((httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()))
                .csrf((csrf) -> csrf.disable())
                .formLogin((httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()));

        return httpSecurity.build();

//        return httpSecurity
//                .authorizeHttpRequests(
//                        authorize -> authorize
//                                .requestMatchers("/users/**").permitAll()
//                                .requestMatchers("/login").permitAll()
//                                .requestMatchers("/health").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
