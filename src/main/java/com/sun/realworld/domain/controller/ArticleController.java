package com.sun.realworld.domain.controller;

import com.sun.realworld.domain.dto.request.CreateArticleRequestDto;
import com.sun.realworld.domain.dto.request.UpdateArticleRequestDto;
import com.sun.realworld.domain.dto.response.ArticleResponseDto;
import com.sun.realworld.domain.model.ArticleQueryParams;
import com.sun.realworld.domain.model.FeedParams;
import com.sun.realworld.domain.service.ArticleService;
import com.sun.realworld.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ArticleResponseDto.Multiple listArticles(
        @ModelAttribute ArticleQueryParams params,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return articleService.listArticles(params, userDetails);
    }

    @GetMapping("/feed")
    public ArticleResponseDto.Multiple feedArticles(
        @ModelAttribute @Valid FeedParams params,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return articleService.feedArticles(params, userDetails);
    }

    @GetMapping("/{slug}")
    public ArticleResponseDto.Single getArticle(
        @PathVariable("slug") String slug,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ArticleResponseDto.Single(articleService.getArticle(slug, userDetails));
    }

    @PostMapping
    public ArticleResponseDto.Single createArticle(
        @RequestBody @Valid CreateArticleRequestDto article,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ArticleResponseDto.Single(articleService.createArticle(article, userDetails));
    }

    @PutMapping("/{slug}")
    public ArticleResponseDto.Single updateArticle(
        @PathVariable("slug") String slug,
        @RequestBody @Valid UpdateArticleRequestDto article,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ArticleResponseDto.Single(articleService.updateArticle(slug, article, userDetails));
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(
        @PathVariable("slug") String slug,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        articleService.deleteArticle(slug, userDetails);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleResponseDto.Single favoriteArticle(
        @PathVariable("slug") String slug,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ArticleResponseDto.Single(articleService.favoriteArticle(slug, userDetails));
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleResponseDto.Single unfavoriteArticle(
        @PathVariable("slug") String slug,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new ArticleResponseDto.Single(articleService.unfavoriteArticle(slug, userDetails));
    }
}
