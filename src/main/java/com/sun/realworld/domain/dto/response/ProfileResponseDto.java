package com.sun.realworld.domain.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {

    private String username;

    private String bio;

    private String image;

    private Boolean following;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Single {
        private ProfileResponseDto profile;
    }
}
