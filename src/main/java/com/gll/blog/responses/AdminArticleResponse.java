package com.gll.blog.responses;

import java.time.LocalDateTime;

public record AdminArticleResponse(
        String id,
        String ownerEmail,
        String title,
        String content,
        String categoryName,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
