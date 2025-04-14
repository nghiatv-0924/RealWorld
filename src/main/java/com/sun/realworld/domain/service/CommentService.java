package com.sun.realworld.domain.service;

import com.sun.realworld.domain.dto.request.CreateCommentRequestDto;
import com.sun.realworld.domain.dto.response.CommentResponseDto;
import com.sun.realworld.security.AppUserDetails;

import java.util.List;

public interface CommentService {

    CommentResponseDto addCommentsToAnArticle(
        final String slug,
        final CreateCommentRequestDto comment,
        final AppUserDetails userDetails
    );

    List<CommentResponseDto> getCommentBySlug(
        final String slug,
        final AppUserDetails userDetails
    );

    void delete(
        final String slug,
        final Long commentId,
        final AppUserDetails userDetails
    );
}
