package com.gll.blog.controllers;

import com.gll.blog.requests.ArticleRequest;
import com.gll.blog.responses.PersonalArticleResponse;
import com.gll.blog.services.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/me/articles")
public class PersonalArticleController {

    private final ArticleService articleService;

    public PersonalArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @PostMapping
    public ResponseEntity<PersonalArticleResponse> createDraft(@AuthenticationPrincipal UserDetails user,
                                                               @RequestBody ArticleRequest request) {
        return new ResponseEntity<>(articleService.createDraftArticle(user, request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PersonalArticleResponse>> readAll(@AuthenticationPrincipal UserDetails user,
                                                                 @PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(articleService.readAllUserArticle(user, pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonalArticleResponse> update(@AuthenticationPrincipal UserDetails user,
                                                          @RequestBody ArticleRequest request,
                                                          @PathVariable("id") UUID id) {
        return new ResponseEntity<>(articleService.updateArticle(user, request, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails user,
                                       @PathVariable("id") UUID id) {
        articleService.deleteArticle(user, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
