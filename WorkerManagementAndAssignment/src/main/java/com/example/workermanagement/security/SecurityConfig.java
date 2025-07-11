package com.example.workermanagement.security;

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
    	http
    	    .cors(cors -> {})
    	    .csrf(csrf -> csrf.disable())
    	    .authorizeHttpRequests(auth -> auth
    	    		
    	    .requestMatchers("/actuator/**","/swagger-ui/**","/v3/api-docs/**","/api/urbanclean/v1/admin/workers").permitAll()
    	    .requestMatchers("/api/urbanclean/v1/admin/workerassignments/assignments/{vehicleId}").hasAnyRole("Admin","Supervisor")
    	    .requestMatchers("/api/v1/urbanclean/worker/store","/api/v1/urbanclean/worker/vehicle/{vehicleId}","/api/v1/urbanclean/worker/zones/{zoneId}","/api/urbanclean/v1/admin/workers/email").hasAnyRole("Admin","Worker")
            .requestMatchers("/api/urbanclean/v1/admin/workers/**","/api/urbanclean/v1/admin/workerassignments/**","/api/urbanclean/v1/assignments/workerassignment","/api/v1/urbanclean/worker/vehicle/{vehicleId}").hasRole("Admin")
            .requestMatchers("/api/v1/urbanclean/worker/**").hasRole("Worker")
            .requestMatchers("/api/v1/urbanclean/supervisor/**").hasRole("Supervisor")
            
            .anyRequest().authenticated())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
