package com.sun.realworld.domain.service;

import com.sun.realworld.domain.dto.response.ProfileResponseDto;
import com.sun.realworld.security.AppUserDetails;

public interface ProfileService {

    ProfileResponseDto getProfile(
        final String username,
        final AppUserDetails userDetails
    );

    ProfileResponseDto followUser(
        final String username,
        final AppUserDetails userDetails
    );

    ProfileResponseDto unfollowUser(
        final String username,
        final AppUserDetails userDetails
    );

    ProfileResponseDto getProfileByUserId(
        final Long userId,
        final AppUserDetails userDetails
    );
}
