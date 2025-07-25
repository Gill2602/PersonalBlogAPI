package com.gll.blog.repositories;

import com.gll.blog.entities.ArticleEntity;
import com.gll.blog.entities.UserEntity;
import com.gll.blog.entities.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArticleRepository extends JpaRepository<ArticleEntity, UUID> {

    Page<ArticleEntity> findByStatusOrderByPublishedAtDesc(Status status, Pageable pageable);

    Page<ArticleEntity> findByUserOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);

    Page<ArticleEntity> findByUserAndStatusOrderByPublishedAtDesc(UserEntity userEntity, Status status, Pageable pageable);
}
