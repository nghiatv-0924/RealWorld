package com.sun.realworld.domain.controller;

import com.sun.realworld.domain.dto.request.AuthenticationRequestDto;
import com.sun.realworld.domain.dto.request.RegistrationRequestDto;
import com.sun.realworld.domain.dto.request.UpdateUserRequestDto;
import com.sun.realworld.domain.dto.response.UserResponseDto;
import com.sun.realworld.domain.service.UserService;
import com.sun.realworld.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public UserResponseDto authentication(@RequestBody @Valid AuthenticationRequestDto user) {
        return userService.authentication(user);
    }

    @PostMapping("/users")
    public UserResponseDto registration(@RequestBody @Valid RegistrationRequestDto user) {
        return userService.registration(user);
    }

    @GetMapping("/user")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal AppUserDetails userDetails) {
        return userService.getCurrentUser(userDetails);
    }

    @PutMapping("/user")
    public UserResponseDto updateUser(
        @RequestBody @Valid UpdateUserRequestDto user,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return userService.updateUser(user, userDetails);
    }
}
