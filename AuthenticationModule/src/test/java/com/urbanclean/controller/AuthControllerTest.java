package com.urbanclean.controller;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
 
import com.urbanclean.dto.LoginRequest;
import com.urbanclean.dto.LoginResponse;
import com.urbanclean.dto.UserDto;
import com.urbanclean.exception.InvalidUserCredentialsException;
import com.urbanclean.service.AuthService;
 
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthControllerTest {
 
    @Mock
    private AuthService authService;
 
    @InjectMocks
    private AuthController authController;
 
    private UserDto userDto;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
 
    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
 
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
 
        loginResponse = new LoginResponse();
        loginResponse.setName("Test User");
        loginResponse.setEmail("test@example.com");
        loginResponse.setToken("sample-token");
 
        // Mock behavior for positive cases
        when(authService.registerUser(any(UserDto.class))).thenReturn("User registered successfully");
        when(authService.loginUser("test@example.com", "password123")).thenReturn(loginResponse);
    }
 
   // Positive Test: Register User
    @Test
    void testRegisterUser_Success() {
        ResponseEntity<String> response = authController.registerUser(userDto);
        assertEquals("User registered successfully", response.getBody());
        verify(authService).registerUser(any(UserDto.class));
    }
 
    //  Register User with Invalid Input
    @Test
    void testRegisterUser_InvalidInput() {
        userDto.setEmail(""); // Invalid email
        when(authService.registerUser(any(UserDto.class))).thenThrow(new IllegalArgumentException("Invalid email"));
 
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authController.registerUser(userDto));
        assertEquals("Invalid email", exception.getMessage());
        verify(authService).registerUser(any(UserDto.class));
    }
 
    //  Login User
    @Test
    void testLoginUser_Success() {
        ResponseEntity<LoginResponse> response = authController.loginUser(loginRequest);
        assertEquals("sample-token", response.getBody().getToken());
        verify(authService).loginUser("test@example.com", "password123");
    }
 
    // Login User with Invalid Credentials
    @Test
    void testLoginUser_InvalidCredentials() {
        when(authService.loginUser("test@example.com", "wrongPassword"))
                .thenThrow(new InvalidUserCredentialsException("Invalid credentials"));
 
        loginRequest.setPassword("wrongPassword");
 
        Exception exception = assertThrows(InvalidUserCredentialsException.class,
                () -> authController.loginUser(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authService).loginUser("test@example.com", "wrongPassword");
    }
 
    //  Test Endpoint
    @Test
    void testTestEndpoint() {
        String response = authController.test();
        assertEquals("hello this is a test case", response);
    }
 
    // Worker Endpoint
    @Test
    void testWorkerEndpoint() {
        String response = authController.test2();
        assertEquals("hello this should be accesed by worker", response);
    }
 
    //  Admin Endpoint
    @Test
    void testAdminEndpoint() {
        String response = authController.test3();
        assertEquals("hello this should be accesed by admin", response);
    }
 
    //  Supervisor Endpoint
    @Test
    void testSupervisorEndpoint() {
        String response = authController.test4();
        assertEquals("hello this should be accesed by Supervisor", response);
    }
}
 
 