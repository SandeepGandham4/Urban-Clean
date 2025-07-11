package com.example.security;

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
//    	    		.requestMatchers(
//    	    			    "/swagger-ui/**",
//    	    			    "/v3/api-docs/**",
//    	    			    "/swagger-resources/**",
//    	    			    "/swagger-ui.html",
//    	    			    "/webjars/**"
//    	    			).permitAll()
    	    .requestMatchers("/actuator/**","/swagger-ui/**","/v3/api-docs/**","/api/v1/urbanclean/admin/zones/{zoneId}/routes").permitAll()
    	    .requestMatchers("/api/v1/urbanclean/admin/zones/allzones","/api/v1/urbanclean/admin/zones/{zoneId}/routes").hasAnyRole("Admin","Supervisor")
            .requestMatchers("/api/v1/urbanclean/admin/zones/**","/api/v1/urbanclean/admin/zones/{zoneId}/routes/**").hasRole("Admin")
            .requestMatchers("/api/v1/worker/**").hasRole("Worker")
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
