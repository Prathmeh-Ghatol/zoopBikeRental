package com.zoopbike.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity(securedEnabled = true,prePostEnabled =true,jsr250Enabled = true
)
public class SecurityConfig {

    @Autowired
    JwtRequestFilter jwtRequestFilter;
    @Autowired
    AuthenticationProvider authenticationProvider;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/application/user/register").permitAll() // Permit all requests to paths starting with /application/user/
                        .requestMatchers("/bikepartner/register").permitAll() // Permit all requests to paths starting with /application/user/
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("bike/search/***").permitAll()// Permit all requests to paths starting with /api/v1/
                        .anyRequest().authenticated()
                )
                .build();
    }


//    }
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider);
//    }
}



