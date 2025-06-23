package com.gll.blog.requests;

public record AuthRequest(
        String email,
        String password
) {
}
