package com.gll.blog.requests;

import com.gll.blog.entities.enums.Status;

public record ArticleRequest(
        String title,
        String content,
        String categoryName,
        Status status
) {
}
