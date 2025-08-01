package com.omnify.gayatri_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Optional: Disable CSRF for APIs
                    .cors(cors -> cors.disable()) // Already added
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/**").permitAll() // Public endpoints
                            .anyRequest().authenticated() // Others need authentication
                    );
            return http.build();
        }
}
