package com.gll.blog.responses;

public record AuthResponse(
        String token,
        UserResponse user
) {
}
