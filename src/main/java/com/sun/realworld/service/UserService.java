package com.sun.realworld.service;

import com.sun.realworld.entity.UserEntity;
import com.sun.realworld.repository.UserRepository;
import com.sun.realworld.response.UserResponse;
import com.sun.realworld.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponse registerUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity newUser = userRepository.save(user);
        return new UserResponse(
            newUser.getEmail(),
            null,
            newUser.getUsername(),
            newUser.getBio(),
            newUser.getImage()
        );
    }

    public UserResponse loginUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UserResponse(
            user.getEmail(),
            jwtUtil.generateToken(user.getEmail()),
            user.getUsername(),
            user.getBio(),
            user.getImage()
        );
    }
}
