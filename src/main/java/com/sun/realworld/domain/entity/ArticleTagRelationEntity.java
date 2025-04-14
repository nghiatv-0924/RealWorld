package com.sun.realworld.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagRelationEntity extends BaseEntity {

    @Column(name = "tag", nullable = false)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article", nullable = false)
    private ArticleEntity article;
}
