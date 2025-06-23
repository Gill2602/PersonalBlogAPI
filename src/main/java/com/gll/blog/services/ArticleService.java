package com.gll.blog.services;

import com.gll.blog.entities.ArticleEntity;
import com.gll.blog.entities.CategoryEntity;
import com.gll.blog.entities.UserEntity;
import com.gll.blog.entities.enums.Status;
import com.gll.blog.exceptions.DataValidityException;
import com.gll.blog.exceptions.NotFoundException;
import com.gll.blog.exceptions.UnauthorizedException;
import com.gll.blog.repositories.ArticleRepository;
import com.gll.blog.repositories.CategoryRepository;
import com.gll.blog.repositories.UserRepository;
import com.gll.blog.requests.ArticleRequest;
import com.gll.blog.responses.AdminArticleResponse;
import com.gll.blog.responses.GlobalArticleResponse;
import com.gll.blog.responses.PersonalArticleResponse;
import com.gll.blog.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    public Page<GlobalArticleResponse> readAllPublicArticle(Pageable pageable) {
        return articleRepository.findByStatus(Status.PUBLISHED, pageable)
                .map(article -> new GlobalArticleResponse(
                        article.getId().toString(),
                        article.getUser().getEmail(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCategory().getName(),
                        article.getPublishedAt()
                ));
    }

    public Page<AdminArticleResponse> readAllPublicArticleForAdmin(Pageable pageable) {
        return articleRepository.findByStatus(Status.PUBLISHED, pageable)
                .map(article -> new AdminArticleResponse(
                        article.getId().toString(),
                        article.getUser().getEmail(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCategory().getName(),
                        article.getPublishedAt(),
                        article.getCreatedAt(),
                        article.getUpdatedAt()
                ));
    }

    public Page<GlobalArticleResponse> readAllUserPublicArticleById(Pageable pageable, UUID id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found user with ID: " + id));

        return articleRepository.findByUserAndStatus(userEntity, Status.PUBLISHED, pageable)
                .map(article -> new GlobalArticleResponse(
                        article.getId().toString(),
                        userEntity.getEmail(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCategory().getName(),
                        article.getPublishedAt()
                ));
    }

    public Page<GlobalArticleResponse> readAllUserPublicArticleByEmail(Pageable pageable, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("not found user with email: " + email));

        return articleRepository.findByUserAndStatus(userEntity, Status.PUBLISHED, pageable)
                .map(article -> new GlobalArticleResponse(
                        article.getId().toString(),
                        userEntity.getEmail(),
                        article.getTitle(),
                        article.getContent(),
                        article.getCategory().getName(),
                        article.getPublishedAt()
                ));
    }

    public PersonalArticleResponse createDraftArticle(UserDetails user, ArticleRequest request) {
        checkDataValidity(request);

        UserEntity userEntity = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new NotFoundException("not found a user with email: " + user.getUsername()));

        CategoryEntity categoryEntity = categoryRepository.findByName(request.categoryName())
                .orElseThrow(() -> new NotFoundException("not found a category with name: " + request.categoryName()));

        ArticleEntity article = ArticleEntity.builder()
                .title(request.title())
                .content(request.content())
                .status(Status.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userEntity)
                .category(categoryEntity)
                .build();

        ArticleEntity savedArticle = articleRepository.save(article);
        log.info("created a new article with title: {} and ID: {} in the category {} by user: {} ",
                savedArticle.getTitle(), savedArticle.getId(), categoryEntity.getName(), userEntity.getEmail()
        );

        return new PersonalArticleResponse(
                savedArticle.getId().toString(),
                savedArticle.getTitle(),
                savedArticle.getContent(),
                savedArticle.getStatus(),
                savedArticle.getCategory().getName(),
                savedArticle.getPublishedAt(),
                savedArticle.getCreatedAt(),
                savedArticle.getUpdatedAt()
        );
    }

    public PersonalArticleResponse updateArticle(UserDetails user, ArticleRequest request, UUID id) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found a article with ID: " + id));

        if (isNotOwner(articleEntity, user)) {
            throw new UnauthorizedException(
                    String.format("user '%s' is not the owner of article ID '%s' and is not authorized to update it.",
                            user.getUsername(), articleEntity.getId())
            );
        }

        Optional.ofNullable(request.title()).ifPresent(articleEntity::setTitle);
        Optional.ofNullable(request.content()).ifPresent(articleEntity::setContent);

        if (request.status() != null) {
            switch (request.status()) {
                case DRAFT -> {
                    articleEntity.setStatus(Status.DRAFT);
                    articleEntity.setPublishedAt(null);
                }
                case PUBLISHED -> {
                    articleEntity.setStatus(Status.PUBLISHED);
                    articleEntity.setPublishedAt(LocalDateTime.now());
                }
            }
        }

        if (request.categoryName() != null) {
            CategoryEntity category = categoryRepository.findByName(request.categoryName())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("not found category with name: '%s', no changes have been made to the category.",
                                    request.categoryName()))
                    );

            articleEntity.setCategory(category);
        }

        articleEntity.setUpdatedAt(LocalDateTime.now());

        ArticleEntity savedArticle = articleRepository.save(articleEntity);
        log.info("updated article with title: {} and ID: {} by user {}",
                savedArticle.getTitle(), savedArticle.getId(), user.getUsername());

        return new PersonalArticleResponse(
                savedArticle.getId().toString(),
                savedArticle.getTitle(),
                savedArticle.getContent(),
                savedArticle.getStatus(),
                savedArticle.getCategory().getName(),
                savedArticle.getPublishedAt(),
                savedArticle.getCreatedAt(),
                savedArticle.getUpdatedAt()
        );
    }

    public Page<PersonalArticleResponse> readAllUserArticle(UserDetails user, Pageable pageable) {

        UserEntity userEntity = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new NotFoundException("not found user with email: " + user.getUsername()));

        return articleRepository.findByUser(userEntity, pageable)
                .map(article -> new PersonalArticleResponse(
                        article.getId().toString(),
                        article.getTitle(),
                        article.getContent(),
                        article.getStatus(),
                        article.getCategory().getName(),
                        article.getPublishedAt(),
                        article.getCreatedAt(),
                        article.getUpdatedAt()
                ));
    }

    public void deleteArticle(UserDetails user, UUID id) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found a article with ID: " + id));

        if (isNotOwner(articleEntity, user)) {
            throw new UnauthorizedException(
                    String.format("user '%s' is not the owner of article ID '%s' and is not authorized to delete it.",
                            user.getUsername(), articleEntity.getId())
            );
        }

        log.info("user {} deleted the article with ID: {}",
                user.getUsername(), articleEntity.getId()
        );
        articleRepository.delete(articleEntity);
    }

    public void deleteAnyArticle(UUID id) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found a article with ID: " + id));

        log.info("deleted the article with ID: {} by admin",
                articleEntity.getId()
        );
        articleRepository.delete(articleEntity);
    }

    private void checkDataValidity(ArticleRequest request) {
        if (AppUtils.isBlank(request.title()) || AppUtils.isBlank(request.content())) {
            throw new DataValidityException("The title or content is blank.");
        }

        if (AppUtils.isBlank(request.categoryName())) {
            throw new DataValidityException("The category name is blank");
        }
    }

    private boolean isNotOwner(ArticleEntity article, UserDetails user) {
        return !Objects.equals(article.getUser().getEmail(), user.getUsername());
    }

}
