package com.gll.blog.responses;

import java.util.UUID;

public record CategoryResponse(
    UUID id,
    String name,
    String description
) {
}
