package com.urbanclean.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.urbanclean.dto.LoginResponse;
import com.urbanclean.dto.UserDto;
import com.urbanclean.dto.WorkerDTO;
import com.urbanclean.entity.Roles;
import com.urbanclean.entity.User;
import com.urbanclean.exception.InvalidUserCredentialsException;
import com.urbanclean.feign.WorkerServiceFeignClient;
import com.urbanclean.repository.RolesRepository;
import com.urbanclean.repository.UserRepository;
import com.urbanclean.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuthService {
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final JwtUtil jwtUtil;
    
    private final WorkerServiceFeignClient workerServiceFeignClient;
 
    // 🔹 Register Users
    public String registerUser(UserDto userDto) {
    	Roles role = null;
        Optional<Roles> roles = rolesRepository.findById(3);
		if(roles.isPresent()) {
			role = roles.get();
		}
		String email = userDto.getEmail().toLowerCase();
		User user1 = userRepository.findByEmail(email);
		if(user1 != null) {
			log.info("user is already registered");
			return "user already registered with this email";
		}
		else {
			User user = new User(userDto.getName(),userDto.getEmail(),passwordEncoder.encode(userDto.getPassword()),role);
			userRepository.save(user);
			WorkerDTO worker=new WorkerDTO();
			worker.setContactInfo(userDto.getEmail());
			worker.setName(userDto.getName());
			worker.setRole(userDto.getRole());
			workerServiceFeignClient.createWorker(worker);
			log.info("user registered Successfully");
	        return "User Registered Successfully!!";
		}	
    }
    
 // 🔹 Login Users & Admins
    public LoginResponse loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
        	String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getRoleName());
            log.info("User login succesfully and token is generated succesfully");
            return  new LoginResponse(user.getName(),email,token,user.getRole().getRoleName());
        }
        log.info("user was not able to login due to invalid credentials");
        throw new InvalidUserCredentialsException("Invalid User Credentials! Please check your username and password ");
    }
}
