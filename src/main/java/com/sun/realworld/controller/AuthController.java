package com.sun.realworld.controller;

import com.sun.realworld.entity.UserEntity;
import com.sun.realworld.request.AuthenticationRequest;
import com.sun.realworld.request.RegistrationRequest;
import com.sun.realworld.response.UserResponse;
import com.sun.realworld.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> register(@RequestBody RegistrationRequest request) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        UserResponse response = userService.registerUser(newUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<UserResponse> login(@RequestBody AuthenticationRequest request) {
        UserResponse response = userService.loginUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
