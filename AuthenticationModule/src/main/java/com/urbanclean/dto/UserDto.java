package com.urbanclean.dto;

import com.urbanclean.enums.WorkerRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	@NotBlank(message = "Name is required")
    @Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters")
	private String name;
	
	@NotBlank(message = "Email is required")
    @Size(min = 2, max = 25, message = "Email must be between 2 and 25 characters")
	@Email(message="Enter a valid email address")
	private String email;
	
	@NotBlank(message = "Password is required")
    @Size(min = 7, max = 25, message = "Password must be between 7 and 25 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String password;
	
	private WorkerRole role; 
	
}
