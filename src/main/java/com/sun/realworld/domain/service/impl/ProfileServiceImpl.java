package com.sun.realworld.domain.service.impl;

import com.sun.realworld.domain.dto.response.ProfileResponseDto;
import com.sun.realworld.domain.entity.FollowEntity;
import com.sun.realworld.domain.entity.UserEntity;
import com.sun.realworld.domain.repository.FollowRepository;
import com.sun.realworld.domain.repository.UserRepository;
import com.sun.realworld.domain.service.ProfileService;
import com.sun.realworld.exception.AppError;
import com.sun.realworld.exception.AppException;
import com.sun.realworld.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public ProfileResponseDto getProfile(String username, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        Boolean following = null;
        if (userDetails != null) {
            following = followRepository.findByFolloweeIdAndFollowerId(
                userEntity.getId(),
                userDetails.getId()
            ).isPresent();
        }

        return convertEntityToDto(userEntity, following);
    }

    @Transactional
    @Override
    public ProfileResponseDto followUser(String username, AppUserDetails userDetails) {
        UserEntity followee = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException((AppError.USER_NOT_FOUND)));
        UserEntity follower = UserEntity.builder()
            .id(userDetails.getId())
            .build();

        followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
            .ifPresent(entity -> {
                throw new AppException(AppError.ALREADY_FOLLOWED_USER);
            });

        FollowEntity followEntity = FollowEntity.builder()
            .followee(followee)
            .follower(follower)
            .build();
        followRepository.save(followEntity);

        return convertEntityToDto(followee, true);
    }

    @Transactional
    @Override
    public ProfileResponseDto unfollowUser(String username, AppUserDetails userDetails) {
        UserEntity followee = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder()
            .id(userDetails.getId())
            .build();

        FollowEntity followEntity = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
            .orElseThrow(() -> new AppException(AppError.FOLLOW_NOT_FOUND));
        followRepository.delete(followEntity);

        return convertEntityToDto(followee, false);
    }

    @Override
    public ProfileResponseDto getProfileByUserId(Long userId, AppUserDetails userDetails) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(AppError.USER_NOT_FOUND));
        Boolean following = null;
        if (userDetails != null) {
            following = followRepository.findByFolloweeIdAndFollowerId(
                userEntity.getId(),
                userDetails.getId()
            ).isPresent();
        }

        return convertEntityToDto(userEntity, following);
    }

    private ProfileResponseDto convertEntityToDto(UserEntity entity, Boolean following) {
        return ProfileResponseDto.builder()
            .username(entity.getUsername())
            .bio(entity.getBio())
            .image(entity.getImage())
            .following(following)
            .build();
    }
}
