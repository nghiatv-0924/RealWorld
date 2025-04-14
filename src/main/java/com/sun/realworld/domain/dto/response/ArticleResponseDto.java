package com.sun.realworld.domain.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private String slug;

    private String title;

    private String description;

    private String body;

    private List<String> tagList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean favorited;

    private Long favoritesCount;

    private ProfileResponseDto author;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Single {
        private ArticleResponseDto article;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Multiple {
        private List<ArticleResponseDto> articles;
        private Long articlesCount;
    }
}
