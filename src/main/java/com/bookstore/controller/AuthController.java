package com.bookstore.controller;

import com.bookstore.dto.JwtResponse;
import com.bookstore.dto.LoginRequest;
import com.bookstore.dto.MessageResponse;
import com.bookstore.dto.SignupRequest;
import com.bookstore.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse response = authService.registerUser(signUpRequest);

        if (response.getMessage().startsWith("Error:")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        // No server-side logout needed for JWT authentication since it's stateless
        // Client just needs to delete the token
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}