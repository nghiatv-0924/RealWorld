package com.sun.realworld.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagListResponseDto {

    private List<String> tags;
}
