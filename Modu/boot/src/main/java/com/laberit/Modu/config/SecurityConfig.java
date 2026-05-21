package com.laberit.Modu.config;

import com.laberit.Modu.rest.advice.RestAuthenticationEntryPoint;
import com.laberit.Modu.security.DeviceIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DeviceIdFilter deviceIdFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**", "/docs/**", "/swagger-ui/**","/swagger-ui.html",
                                "/swagger-resources/**",
                                "/product/**", "/products/**",
                                "/categories").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(deviceIdFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
