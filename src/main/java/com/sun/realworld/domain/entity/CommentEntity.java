package com.sun.realworld.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NamedEntityGraph(
    name = "fetch-author",
    attributeNodes = @NamedAttributeNode("author")
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseEntity {

    @Column(nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private ArticleEntity article;
}
