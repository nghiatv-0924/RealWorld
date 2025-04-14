package com.sun.realworld.domain.repository;

import com.sun.realworld.domain.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findByArticleIdAndUserId(Long articleId, Long userId);
}
