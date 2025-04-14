package com.sun.realworld.domain.service.impl;

import com.sun.realworld.domain.dto.request.AuthenticationRequestDto;
import com.sun.realworld.domain.dto.request.RegistrationRequestDto;
import com.sun.realworld.domain.dto.request.UpdateUserRequestDto;
import com.sun.realworld.domain.dto.response.UserResponseDto;
import com.sun.realworld.domain.entity.UserEntity;
import com.sun.realworld.domain.repository.UserRepository;
import com.sun.realworld.domain.service.UserService;
import com.sun.realworld.exception.AppError;
import com.sun.realworld.exception.AppException;
import com.sun.realworld.security.AppUserDetails;
import com.sun.realworld.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto authentication(AuthenticationRequestDto user) {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail())
            .filter(entity -> passwordEncoder.matches(user.getPassword(), entity.getPassword()))
            .orElseThrow(() -> new AppException(AppError.LOGIN_INFO_INVALID));
        return convertEntityToDto(userEntity);
    }

    @Override
    public UserResponseDto registration(final RegistrationRequestDto user) {
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail())
            .stream()
            .findAny()
            .ifPresent(entity -> {
                throw new AppException(AppError.DUPLICATED_USER);
            });
        UserEntity userEntity = UserEntity.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .password(passwordEncoder.encode(user.getPassword()))
            .bio("")
            .build();
        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getCurrentUser(AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        return convertEntityToDto(userEntity);
    }

    @Override
    public UserResponseDto updateUser(UpdateUserRequestDto user, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));

        if (user.getUsername() != null) {
            userRepository.findByUsername(user.getUsername())
                .filter(entity -> !entity.getId().equals(userEntity.getId()))
                .ifPresent(entity -> {
                    throw new AppException(AppError.DUPLICATED_USER);
                });
            userEntity.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            userRepository.findByEmail(user.getEmail())
                .filter(entity -> !entity.getId().equals(userEntity.getId()))
                .ifPresent(entity -> {
                    throw new AppException(AppError.DUPLICATED_USER);
                });
            userEntity.setEmail(user.getEmail());
        }

        if (user.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getBio() != null) {
            userEntity.setBio(user.getBio());
        }

        if (user.getImage() != null) {
            userEntity.setImage(user.getImage());
        }

        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
    }

    private UserResponseDto convertEntityToDto(UserEntity entity) {
        return UserResponseDto.builder()
            .username(entity.getUsername())
            .bio(entity.getBio())
            .email(entity.getEmail())
            .image(entity.getImage())
            .token(jwtUtils.encode(entity.getEmail()))
            .build();
    }
}
