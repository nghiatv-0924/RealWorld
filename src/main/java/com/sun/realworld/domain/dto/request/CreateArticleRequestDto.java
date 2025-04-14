package com.sun.realworld.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequestDto {

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotEmpty(message = "Body cannot be empty")
    private String body;

    private List<String> tagList;
}
