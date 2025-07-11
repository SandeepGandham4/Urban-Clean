package com.urbanclean.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanclean.dto.LoginRequest;
import com.urbanclean.dto.LoginResponse;
import com.urbanclean.dto.UserDto;
import com.urbanclean.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/urbanclean/v1/auth")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
	
	

    // 🔹 Handles registration for Users & Admins
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }
    
    @GetMapping("/test")
    public String test() {
    	return "hello this is a test case";
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    	
    	
    	return ResponseEntity.ok(authService.loginUser(loginRequest.getEmail(), loginRequest.getPassword()));
    }
    
    
    @GetMapping("worker/test2")
    public String test2(){
    	return "hello this should be accesed by worker";
    }
    @GetMapping("admin/test3")
    public String test3(){
    	return "hello this should be accesed by admin";
    }
    @GetMapping("supervisor/test4")
    public String test4() {
    	return "hello this should be accesed by Supervisor";
    }
    
}
