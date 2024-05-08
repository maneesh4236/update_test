package com.socgen.weather.service;

import com.socgen.weather.controller.AuthenticationRequest;
import com.socgen.weather.controller.AuthenticationResponce;  // Fixed class name
import com.socgen.weather.controller.RegisterRequest;
import com.socgen.weather.entity.Role;
import com.socgen.weather.entity.User;
import com.socgen.weather.exceptions.EmailAlreadyExistsException;
import com.socgen.weather.exceptions.MissingFieldException;
import com.socgen.weather.exceptions.UserNotFoundException;  // New custom exception
import com.socgen.weather.exceptions.WrongPasswordException;
import com.socgen.weather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponce register(RegisterRequest request) {
        // Validate required fields (name, email, password)
        if (request.getName() == null || request.getName().isBlank()) {
            throw new MissingFieldException("Please enter a name");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new MissingFieldException("Please enter an email");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new MissingFieldException("Please enter a password");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Create a new user and save to the repository
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        // Generate a JWT token for the new user
        String jwtToken = jwtService.generateToken(user);

        // Return an authentication response with the token
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponce authenticate(AuthenticationRequest request) {
        // Validate required fields (email, password)
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new MissingFieldException("Please enter an email");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new MissingFieldException("Please enter a password");
        }

        // Check if the user exists by email
        var user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        // Attempt to authenticate with the given credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()  // Provided password
                    )
            );
        } catch (BadCredentialsException e) {
            throw new WrongPasswordException("Wrong password entered");  // Password mismatch
        }

        // Generate a JWT token
        String jwtToken = jwtService.generateToken(user.get());

        // Return an authentication response with the token
        return AuthenticationResponce.builder()
                .token(jwtToken)
                .build();
    }
}
