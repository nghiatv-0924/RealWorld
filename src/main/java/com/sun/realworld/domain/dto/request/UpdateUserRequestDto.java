package com.sun.realworld.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    private String email;

    private String username;

    private String password;

    private String image;

    private String bio;
}
