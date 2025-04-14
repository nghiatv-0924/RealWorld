package com.sun.realworld.domain.controller;

import com.sun.realworld.domain.dto.response.TagListResponseDto;
import com.sun.realworld.domain.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public TagListResponseDto getTags() {
        return new TagListResponseDto(tagService.getTags());
    }
}
