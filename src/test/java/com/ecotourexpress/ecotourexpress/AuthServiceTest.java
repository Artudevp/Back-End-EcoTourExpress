package com.ecotourexpress.ecotourexpress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecotourexpress.ecotourexpress.Auth.AuthResponse;
import com.ecotourexpress.ecotourexpress.Auth.AuthService;
import com.ecotourexpress.ecotourexpress.Auth.LoginRequest;
import com.ecotourexpress.ecotourexpress.Jwt.JwtService;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.model.dto.UserDTO;
import com.ecotourexpress.ecotourexpress.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testLoginSuccess() {
        String username = "Artu1091";
        String password = "314266799xL";
        LoginRequest request = new LoginRequest(username, password);
        
        User mockUser = new User();
        mockUser.setUsername(username);
        
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtService.getToken(any(UserDTO.class))).thenReturn("test-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("test-token", response.getToken());
    }

    @Test
    public void testLoginUserNotFound() {
        String username = "unknown";
        String password = "password";
        LoginRequest request = new LoginRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(request));
    }
}
