package com.gll.blog.mappers;

import com.gll.blog.entities.ArticleEntity;
import com.gll.blog.responses.AdminArticleResponse;
import com.gll.blog.responses.GlobalArticleResponse;
import com.gll.blog.responses.PersonalArticleResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public AdminArticleResponse toAdminResponse(ArticleEntity article) {
        return new AdminArticleResponse(
                article.getId().toString(),
                article.getUser().getEmail(),
                article.getTitle(),
                article.getContent(),
                article.getCategory().getName(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }

    public GlobalArticleResponse toGlobalResponse(ArticleEntity article, String userEmail) {
        return new GlobalArticleResponse(
                article.getId().toString(),
                userEmail,
                article.getTitle(),
                article.getContent(),
                article.getCategory().getName(),
                article.getPublishedAt()
        );
    }

    public GlobalArticleResponse toGlobalResponse(ArticleEntity article) {
        return new GlobalArticleResponse(
                article.getId().toString(),
                article.getUser().getEmail(),
                article.getTitle(),
                article.getContent(),
                article.getCategory().getName(),
                article.getPublishedAt()
        );
    }

    public PersonalArticleResponse toPersonalResponse(ArticleEntity article) {
        return new PersonalArticleResponse(
                article.getId().toString(),
                article.getTitle(),
                article.getContent(),
                article.getStatus(),
                article.getCategory().getName(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
