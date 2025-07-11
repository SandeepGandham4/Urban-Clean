package com.urbanclean.repository;
 
import com.urbanclean.entity.Roles;
import com.urbanclean.entity.User;
import com.urbanclean.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
//import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthRepositoryTest {
 
    @Mock
    private UserRepository userRepository;
 
    private User user1;
    private User user2;
 
    @BeforeEach
    void setUp() {
        Roles role = new Roles();
        role.setRoleId(1);
        role.setRoleName("USER");
 
        user1 = new User();
        user1.setId(1);
        user1.setName("Test User 1");
        user1.setEmail("test1@example.com");
        user1.setPassword("password123");
        user1.setRole(role);
 
        user2 = new User();
        user2.setId(2);
        user2.setName("Test User 2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password456");
        user2.setRole(role);
 
        // Mock behavior
        when(userRepository.findByEmail("test1@example.com")).thenReturn(user1);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
    }
 
    @Test
    void testFindByEmail_UserExists() {
        User foundUser = userRepository.findByEmail("test1@example.com");
        assertNotNull(foundUser);
        assertEquals("Test User 1", foundUser.getName());
        verify(userRepository, times(1)).findByEmail("test1@example.com");
    }
 
    @Test
    void testFindByEmail_UserDoesNotExist() {
        User foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
 
    @Test
    void testFindById_UserExists() {
        Optional<User> foundUser = userRepository.findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals("Test User 1", foundUser.get().getName());
        verify(userRepository, times(1)).findById(1);
    }
 
    @Test
    void testFindById_UserDoesNotExist() {
        Optional<User> foundUser = userRepository.findById(999);
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(999);
    }
 
    @Test
    void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertEquals("Test User 1", users.get(0).getName());
        assertEquals("Test User 2", users.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }
}
 
 
 