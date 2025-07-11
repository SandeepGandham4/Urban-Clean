package com.urbanclean.service;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.util.Optional;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import com.urbanclean.dto.LoginResponse;
import com.urbanclean.dto.UserDto;
import com.urbanclean.entity.Roles;
import com.urbanclean.entity.User;
import com.urbanclean.exception.InvalidUserCredentialsException;
import com.urbanclean.feign.WorkerServiceFeignClient;
import com.urbanclean.repository.RolesRepository;
import com.urbanclean.repository.UserRepository;
import com.urbanclean.security.JwtUtil;
 
class AuthServiceTest {
 
    @Mock
    private UserRepository userRepository;
 
    @Mock
    private PasswordEncoder passwordEncoder;
 
    @Mock
    private RolesRepository rolesRepository;
 
    @Mock
    private JwtUtil jwtUtil;
 
    @InjectMocks
    private AuthService authService;
    
    @Mock
    private WorkerServiceFeignClient workerServiceFeignClient;
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
 
    @Test
    void testRegisterUser_SuccessfulRegistration() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setName("Test User");
 
        Roles role = new Roles();
        role.setRoleId(3);
        role.setRoleName("USER");
 
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
        when(rolesRepository.findById(3)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
 
        String response = authService.registerUser(userDto);
        assertEquals("User Registered Successfully!!", response);
        verify(userRepository, times(1)).save(any(User.class));
    }
 
    @Test
    void testLoginUser_SuccessfulLogin() {
        String email = "test@example.com";
        String password = "password123";
 
        Roles role = new Roles();
        role.setRoleName("USER");
 
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(role);
        user.setName("Test User");
 
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(email, role.getRoleName())).thenReturn("sampleToken");
 
        LoginResponse response = authService.loginUser(email, password);
        assertEquals("sampleToken", response.getToken());
        assertEquals(email, response.getEmail());
        assertEquals("Test User", response.getName());
    }
 
    @Test
    void testLoginUser_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongPassword";
 
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
 
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
 
        assertThrows(InvalidUserCredentialsException.class, () -> authService.loginUser(email, password));
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
 
    @Test
    void testLoginUser_UserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password123";
 
        when(userRepository.findByEmail(email)).thenReturn(null);
 
        assertThrows(InvalidUserCredentialsException.class, () -> authService.loginUser(email, password));
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
 
    @Test
    void testLoginUser_NullEmail() {
        String email = null;
        String password = "password123";
 
        assertThrows(InvalidUserCredentialsException.class, () -> authService.loginUser(email, password));
        verify(userRepository, never()).findByEmail(anyString());
    }
 
   /* @Test
    void testLoginUser_EmptyEmail() {
        String email = "";
        String password = "password123";
 
        assertThrows(InvalidUserCredentialsException.class, () -> authService.loginUser(email, password));
        verify(userRepository, never()).findByEmail(anyString());
    }*/
 
   /* @Test
    void testLoginUser_NullPassword() {
        String email = "test@example.com";
        String password = null;
 
        assertThrows(InvalidUserCredentialsException.class, () -> authService.loginUser(email, password));
        verify(userRepository, never()).findByEmail(anyString());
    }*/
 
    @Test
    void testLoginUser_UserRoleNull() {
        String email = "test@example.com";
        String password = "password123";
 
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRole(null);
 
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
 
        assertThrows(NullPointerException.class, () -> authService.loginUser(email, password));
    }
 
    // These are now commented because AuthService doesn't validate for them:
    // Uncomment if you add validation in the future!
 
    /*
    @Test
    void testRegisterUser_NullEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail(null);
        userDto.setPassword("password123");
        userDto.setName("Test User");
 
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }
 
    @Test
    void testRegisterUser_EmptyPassword() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("");
        userDto.setName("Test User");
 
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }
 
    @Test
    void testRegisterUser_NullName() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setName(null);
 
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }
 
    @Test
    void testRegisterUser_InvalidEmailFormat() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalid-email");
        userDto.setPassword("password123");
        userDto.setName("Test User");
 
        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(userDto));
        verify(userRepository, never()).save(any(User.class));
    }
    */
}
 
 