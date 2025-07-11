package com.urbanclean.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
	
	@NotNull(message="Email should not be null")
	@NotBlank(message = "Email is required")
    @Size(min = 2, max = 25, message = "Email must be between 2 and 25 characters")
	private String  email;
	
	@NotNull(message="Password should not be null")
	@NotBlank(message = "password is required")
    @Size(min = 2, max = 25, message = "password must be between 2 and 25 characters")
	private String password;
}

