package com.sun.realworld.domain.service.impl;

import com.sun.realworld.domain.entity.ArticleTagRelationEntity;
import com.sun.realworld.domain.repository.TagRepository;
import com.sun.realworld.domain.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<String> getTags() {
        return tagRepository.findAll()
            .stream()
            .map(ArticleTagRelationEntity::getTag)
            .distinct()
            .collect(Collectors.toList());
    }
}
