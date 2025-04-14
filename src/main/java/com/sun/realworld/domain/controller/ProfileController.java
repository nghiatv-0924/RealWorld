package com.sun.realworld.domain.controller;

import com.sun.realworld.domain.dto.response.ProfileResponseDto;
import com.sun.realworld.domain.service.ProfileService;
import com.sun.realworld.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileResponseDto.Single getProfile(
        @PathVariable("username") String username,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ProfileResponseDto.Single(profileService.getProfile(username, userDetails));
    }

    @PostMapping("/{username}/follow")
    public ProfileResponseDto.Single followerUser(
        @PathVariable("username") String username,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ProfileResponseDto.Single(profileService.followUser(username, userDetails));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileResponseDto.Single unfollowUser(
        @PathVariable("username") String username,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ProfileResponseDto.Single(profileService.unfollowUser(username, userDetails));
    }
}
