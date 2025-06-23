package com.gll.blog.controllers;

import com.gll.blog.responses.AdminArticleResponse;
import com.gll.blog.responses.GlobalArticleResponse;
import com.gll.blog.services.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/articles")
public class GlobalArticleController {
    
    private final ArticleService articleService;

    public GlobalArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<Page<GlobalArticleResponse>> readAllPublic(@PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(articleService.readAllPublicArticle(pageable), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<AdminArticleResponse>> readAllPublicAdmin(@PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(articleService.readAllPublicArticleForAdmin(pageable), HttpStatus.OK);
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity<Page<GlobalArticleResponse>> readAllUserPublicByID(@PageableDefault(size = 20) Pageable pageable,
                                                                               @PathVariable("id") UUID id) {
        return new ResponseEntity<>(articleService.readAllUserPublicArticleById(pageable, id), HttpStatus.OK);
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<Page<GlobalArticleResponse>> readAllUserPublicByEmail(@PageableDefault(size = 20) Pageable pageable,
                                                                                  @PathVariable("email") String email) {
        return new ResponseEntity<>(articleService.readAllUserPublicArticleByEmail(pageable, email), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") UUID id) {
        articleService.deleteAnyArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
