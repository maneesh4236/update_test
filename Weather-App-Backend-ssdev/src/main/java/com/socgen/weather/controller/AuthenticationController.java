package com.socgen.weather.controller;

import com.socgen.weather.controller.AuthenticationRequest;  // Ensure correct imports
import com.socgen.weather.controller.AuthenticationResponce;  // Fixed the class name
import com.socgen.weather.controller.AuthenticationRequest;
import com.socgen.weather.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponce> register(@RequestBody RegisterRequest request) {

        var response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody AuthenticationRequest request) {

        var response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);

    }
}

