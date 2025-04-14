package com.sun.realworld.domain.service;

import com.sun.realworld.domain.dto.request.AuthenticationRequestDto;
import com.sun.realworld.domain.dto.request.RegistrationRequestDto;
import com.sun.realworld.domain.dto.request.UpdateUserRequestDto;
import com.sun.realworld.domain.dto.response.UserResponseDto;
import com.sun.realworld.security.AppUserDetails;

public interface UserService {

    UserResponseDto authentication(final AuthenticationRequestDto user);

    UserResponseDto registration(final RegistrationRequestDto user);

    UserResponseDto getCurrentUser(final AppUserDetails userDetails);

    UserResponseDto updateUser(final UpdateUserRequestDto user, final AppUserDetails userDetails);
}
