package com.gll.blog.responses;

import com.gll.blog.entities.enums.Status;

import java.time.LocalDateTime;

public record PersonalArticleResponse(
        String id,
        String title,
        String content,
        Status status,
        String categoryName,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
