package com.sun.realworld.domain.service.impl;

import com.sun.realworld.domain.dto.request.CreateArticleRequestDto;
import com.sun.realworld.domain.dto.request.UpdateArticleRequestDto;
import com.sun.realworld.domain.dto.response.ArticleResponseDto;
import com.sun.realworld.domain.dto.response.ProfileResponseDto;
import com.sun.realworld.domain.entity.*;
import com.sun.realworld.domain.model.ArticleQueryParams;
import com.sun.realworld.domain.model.FeedParams;
import com.sun.realworld.domain.repository.ArticleRepository;
import com.sun.realworld.domain.repository.FavoriteRepository;
import com.sun.realworld.domain.repository.FollowRepository;
import com.sun.realworld.domain.service.ArticleService;
import com.sun.realworld.domain.service.ProfileService;
import com.sun.realworld.exception.AppError;
import com.sun.realworld.exception.AppException;
import com.sun.realworld.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final FavoriteRepository favoriteRepository;
    private final FollowRepository followRepository;
    private final ProfileService profileService;

    @Transactional(readOnly = true)
    @Override
    public ArticleResponseDto.Multiple listArticles(ArticleQueryParams params, AppUserDetails userDetails) {
        Pageable pageable = null;
        if (params.getOffset() != null) {
            pageable = PageRequest.of(params.getOffset(), params.getLimit());
        }

        Page<ArticleEntity> articleEntities;
        if (params.getTag() != null) {
            articleEntities = articleRepository.findByTag(params.getTag(), pageable);
        } else if (params.getAuthor() != null) {
            articleEntities = articleRepository.findByAuthorName(params.getAuthor(), pageable);
        } else if (params.getFavorited() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(params.getFavorited(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return new ArticleResponseDto.Multiple(
            convertToArticleList(articleEntities, userDetails),
            articleEntities.getTotalElements()
        );
    }

    @Override
    public ArticleResponseDto.Multiple feedArticles(FeedParams params, AppUserDetails userDetails) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(userDetails.getId())
            .stream()
            .map(FollowEntity::getFollower)
            .map(BaseEntity::getId)
            .collect(Collectors.toList());
        Page<ArticleEntity> articleEntities = articleRepository.findByAuthorIdInOrderByCreatedAtDesc(
            feedAuthorIds,
            PageRequest.of(params.getOffset(), params.getLimit())
        );

        return new ArticleResponseDto.Multiple(
            convertToArticleList(articleEntities, userDetails),
            articleEntities.getTotalElements()
        );
    }

    @Override
    public ArticleResponseDto getArticle(String slug, AppUserDetails userDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));
        List<FavoriteEntity> favoriteEntities = found.getFavoriteList();
        Boolean favorited = null;
        if (userDetails != null) {
            favorited = favoriteEntities.stream()
                .anyMatch(entity -> entity.getUser().getId().equals(userDetails.getId()));
        }
        int favoriteCount = favoriteEntities.size();
        return convertEntityToDto(found, favorited, (long) favoriteCount, userDetails);
    }

    @Transactional
    @Override
    public ArticleResponseDto createArticle(CreateArticleRequestDto article, AppUserDetails userDetails) {
        String slug = String.join("-", article.getTitle().split(" "));
        UserEntity author = UserEntity.builder()
            .id(userDetails.getId())
            .build();

        ArticleEntity articleEntity = ArticleEntity.builder()
            .slug(slug)
            .title(article.getTitle())
            .description(article.getDescription())
            .body(article.getBody())
            .author(author)
            .build();
        List<ArticleTagRelationEntity> tagList = new ArrayList<>();
        for (String tag : article.getTagList()) {
            tagList.add(
                ArticleTagRelationEntity.builder()
                    .article(articleEntity)
                    .tag(tag)
                    .build()
            );
        }
        articleEntity.setTagList(tagList);

        articleEntity = articleRepository.save(articleEntity);
        return convertEntityToDto(articleEntity, false, 0L, userDetails);
    }

    @Transactional
    @Override
    public ArticleResponseDto updateArticle(String slug, UpdateArticleRequestDto article, AppUserDetails userDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug)
            .filter(entity -> entity.getAuthor().getId().equals(userDetails.getId()))
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));

        if (article.getTitle() != null) {
            String newSlug = String.join("-", article.getTitle().split(" "));
            found.setTitle(article.getTitle());
            found.setSlug(newSlug);
        }

        if (article.getDescription() != null) {
            found.setDescription(article.getDescription());
        }

        if (article.getBody() != null) {
            found.setBody(article.getBody());
        }

        articleRepository.save(found);

        return getArticle(slug, userDetails);
    }

    @Transactional
    @Override
    public void deleteArticle(String slug, AppUserDetails userDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug)
            .filter(entity -> entity.getAuthor().getId().equals(userDetails.getId()))
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));
        articleRepository.delete(found);
    }

    @Transactional
    @Override
    public ArticleResponseDto favoriteArticle(String slug, AppUserDetails userDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));

        favoriteRepository.findByArticleIdAndUserId(found.getId(), userDetails.getId())
            .ifPresent(entity -> {
                throw new AppException(AppError.ALREADY_FAVORITED_ARTICLE);
            });

        FavoriteEntity favoriteEntity = FavoriteEntity.builder()
            .article(found)
            .user(
                UserEntity.builder()
                    .id(userDetails.getId())
                    .build()
            )
            .build();
        favoriteRepository.save(favoriteEntity);

        return getArticle(slug, userDetails);
    }

    @Transactional
    @Override
    public ArticleResponseDto unfavoriteArticle(String slug, AppUserDetails userDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));
        FavoriteEntity favoriteEntity = found.getFavoriteList()
            .stream()
            .filter(entity ->
                entity.getArticle().getId().equals(found.getId()) &&
                    entity.getUser().getId().equals(userDetails.getId())
            )
            .findAny()
            .orElseThrow(() -> new AppException(AppError.FAVORITE_NOT_FOUND));
        found.getFavoriteList().remove(favoriteEntity);
        favoriteRepository.delete(favoriteEntity);
        return getArticle(slug, userDetails);
    }

    private ArticleResponseDto convertEntityToDto(
        ArticleEntity entity,
        Boolean favorited,
        Long favoriteCount,
        AppUserDetails userDetails
    ) {
        ProfileResponseDto author = profileService.getProfileByUserId(entity.getAuthor().getId(), userDetails);
        return ArticleResponseDto.builder()
            .slug(entity.getSlug())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .body(entity.getBody())
            .author(author)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .favorited(favorited)
            .favoritesCount(favoriteCount)
            .tagList(
                entity.getTagList().stream()
                    .map(ArticleTagRelationEntity::getTag)
                    .collect(Collectors.toList())
            )
            .build();
    }

    private List<ArticleResponseDto> convertToArticleList(
        Page<ArticleEntity> articleEntities,
        AppUserDetails userDetails
    ) {
        return articleEntities.stream()
            .map(entity -> {
                List<FavoriteEntity> favorites = entity.getFavoriteList();
                Boolean favorited = null;
                if (userDetails != null) {
                    favorited = favorites.stream()
                        .anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(userDetails.getId()));
                }
                int favoriteCount = favorites.size();
                return convertEntityToDto(entity, favorited, (long) favoriteCount, userDetails);
            })
            .collect(Collectors.toList());
    }
}
