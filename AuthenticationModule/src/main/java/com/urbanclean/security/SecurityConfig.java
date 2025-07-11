package com.urbanclean.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf(csrf -> csrf.disable())
    	    .authorizeHttpRequests(auth -> auth
    	    		
    	    .requestMatchers("/api/urbanclean/v1/auth/register/user","/api/urbanclean/v1/auth/test","/api/urbanclean/v1/auth/login","/actuator/**","/swagger-ui/**","/v3/apidocs/**").permitAll()
            .requestMatchers("/api/urbanclean/v1/admin/**").hasRole("Admin")
            .requestMatchers("/api/urbanclean/v1/worker/**").hasRole("Worker")
            .requestMatchers("/api/urbanclean/v1/supervisor/**").hasRole("Supervisor")
            
            .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
