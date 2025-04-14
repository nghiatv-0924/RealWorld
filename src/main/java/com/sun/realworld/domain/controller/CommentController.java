package com.sun.realworld.domain.controller;

import com.sun.realworld.domain.dto.request.CreateCommentRequestDto;
import com.sun.realworld.domain.dto.response.CommentResponseDto;
import com.sun.realworld.domain.service.CommentService;
import com.sun.realworld.security.AppUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{slug}/comments")
    public CommentResponseDto.Single addCommentsToAnArticle(
        @PathVariable("slug") String slug,
        @RequestBody @Valid CreateCommentRequestDto comment,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new CommentResponseDto.Single(commentService.addCommentsToAnArticle(slug, comment, userDetails));
    }

    @GetMapping("/{slug}/comments")
    public CommentResponseDto.Multiple getCommentsFromAnArticle(
        @PathVariable("slug") String slug,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return new CommentResponseDto.Multiple(commentService.getCommentBySlug(slug, userDetails));
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public void deleteComment(
        @PathVariable("slug") String slug,
        @PathVariable("commentId") Long commentId,
        @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        commentService.delete(slug, commentId, userDetails);
    }
}
