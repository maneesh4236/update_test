package com.socgen.weather;


import com.socgen.weather.controller.AuthenticationRequest;
import com.socgen.weather.controller.RegisterRequest;
import com.socgen.weather.entity.User;
import com.socgen.weather.exceptions.EmailAlreadyExistsException;
import com.socgen.weather.exceptions.MissingFieldException;
import com.socgen.weather.exceptions.UserNotFoundException;
import com.socgen.weather.exceptions.WrongPasswordException;
import com.socgen.weather.repository.UserRepository;
import com.socgen.weather.service.AuthenticationService;
import com.socgen.weather.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void testRegister_withMissingFields() {
        RegisterRequest request = new RegisterRequest();  // Missing all fields
        assertThrows(MissingFieldException.class, () -> authenticationService.register(request));
    }

//    @Test
//    void testRegister_withExistingEmail() {
//        RegisterRequest request = new RegisterRequest();
//        request.setEmail("test@example.com");
//
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));  // Simulate existing email
//
//        assertThrows(EmailAlreadyExistsException.class, () -> authenticationService.register(request));
//    }

    @Test
    void testRegister_successful() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");

        var response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
    }

    @Test
    void testAuthenticate_withMissingEmail() {
        AuthenticationRequest request = new AuthenticationRequest();  // Missing email and password
        assertThrows(MissingFieldException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticate_withUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());  // Simulate no user found

        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticate_withWrongPassword() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrong_password");

        var user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(WrongPasswordException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticate_successful() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        var user = new User();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt_token");

        var response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());
    }
}



