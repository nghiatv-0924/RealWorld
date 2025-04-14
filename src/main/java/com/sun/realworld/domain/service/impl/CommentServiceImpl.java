package com.sun.realworld.domain.service.impl;

import com.sun.realworld.domain.dto.request.CreateCommentRequestDto;
import com.sun.realworld.domain.dto.response.CommentResponseDto;
import com.sun.realworld.domain.dto.response.ProfileResponseDto;
import com.sun.realworld.domain.entity.ArticleEntity;
import com.sun.realworld.domain.entity.BaseEntity;
import com.sun.realworld.domain.entity.CommentEntity;
import com.sun.realworld.domain.entity.UserEntity;
import com.sun.realworld.domain.repository.ArticleRepository;
import com.sun.realworld.domain.repository.CommentRepository;
import com.sun.realworld.domain.service.CommentService;
import com.sun.realworld.domain.service.ProfileService;
import com.sun.realworld.exception.AppError;
import com.sun.realworld.exception.AppException;
import com.sun.realworld.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ProfileService profileService;

    @Override
    public CommentResponseDto addCommentsToAnArticle(
        String slug,
        CreateCommentRequestDto comment,
        AppUserDetails userDetails
    ) {
        ArticleEntity articleEntity = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
            .body(comment.getBody())
            .author(
                UserEntity.builder()
                    .id(userDetails.getId())
                    .build()
            )
            .article(articleEntity)
            .build();
        commentRepository.save(commentEntity);

        return convertEntityToDto(commentEntity, userDetails);
    }

    @Override
    public List<CommentResponseDto> getCommentBySlug(String slug, AppUserDetails userDetails) {
        Long articleId = articleRepository.findBySlug(slug)
            .map(BaseEntity::getId)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));

        List<CommentEntity> commentEntities = commentRepository.findByArticleId(articleId);
        return commentEntities.stream()
            .map(entity -> convertEntityToDto(entity, userDetails))
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(String slug, Long commentId, AppUserDetails userDetails) {
        Long articleId = articleRepository.findBySlug(slug)
            .map(BaseEntity::getId)
            .orElseThrow(() -> new AppException(AppError.ARTICLE_NOT_FOUND));

        CommentEntity commentEntity = commentRepository.findById(commentId)
            .filter(entity -> entity.getArticle().getId().equals(articleId))
            .orElseThrow(() -> new AppException(AppError.COMMENT_NOT_FOUND));

        commentRepository.delete(commentEntity);
    }

    private CommentResponseDto convertEntityToDto(CommentEntity entity, AppUserDetails userDetails) {
        ProfileResponseDto author = profileService.getProfileByUserId(
            entity.getAuthor().getId(),
            userDetails
        );
        return CommentResponseDto.builder()
            .id(entity.getId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .body(entity.getBody())
            .author(author)
            .build();
    }
}
