package com.gll.blog.responses;

import com.gll.blog.entities.enums.Role;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID id,
        Role role,
        String email,
        String firstName,
        String lastName,
        LocalDate dateOfBirth
) {
}
