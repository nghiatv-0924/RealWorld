package com.sun.realworld.domain.service;

import com.sun.realworld.domain.dto.request.CreateArticleRequestDto;
import com.sun.realworld.domain.dto.request.UpdateArticleRequestDto;
import com.sun.realworld.domain.dto.response.ArticleResponseDto;
import com.sun.realworld.domain.model.ArticleQueryParams;
import com.sun.realworld.domain.model.FeedParams;
import com.sun.realworld.security.AppUserDetails;

public interface ArticleService {

    ArticleResponseDto.Multiple listArticles(
        final ArticleQueryParams params,
        final AppUserDetails userDetails
    );

    ArticleResponseDto.Multiple feedArticles(
        final FeedParams params,
        final AppUserDetails userDetails
    );

    ArticleResponseDto getArticle(
        final String slug,
        final AppUserDetails userDetails
    );

    ArticleResponseDto createArticle(
        final CreateArticleRequestDto article,
        final AppUserDetails userDetails
    );

    ArticleResponseDto updateArticle(
        final String slug,
        final UpdateArticleRequestDto article,
        final AppUserDetails userDetails
    );

    void deleteArticle(
        final String slug,
        final AppUserDetails userDetails
    );

    ArticleResponseDto favoriteArticle(
        final String slug,
        final AppUserDetails userDetails
    );

    ArticleResponseDto unfavoriteArticle(
        final String slug,
        final AppUserDetails userDetails
    );
}
