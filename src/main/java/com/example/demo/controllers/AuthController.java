package com.example.demo.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTOs.LoginDTO;
import com.example.demo.DTOs.UserCompleteDTO;
import com.example.demo.entities.User;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JwtService;




@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }




    // Registering a new user
    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<User> register(@RequestBody UserCompleteDTO userCompleteDTO) {
        User user = authenticationService.signup(userCompleteDTO);
        return ResponseEntity.ok(user);
        // change this to UserDTO
    }

    // Login User
    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDTO loginDTO) {
        User authenticatedUser = authenticationService.authenticate(loginDTO);
        String token = jwtService.generateToken(authenticatedUser);
        LoginResponse loginRespose = new LoginResponse(token, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginRespose);
        
    }

    // Logout User
    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        // Implement logic to logout user
        return ResponseEntity.ok("Logout successful");
    }

    
    public class LoginResponse {
        private String token;

        private long expiresIn;

        public LoginResponse(String token, long expiresIn) {
            this.token = token;
            this.expiresIn = expiresIn;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
        
    }
}

