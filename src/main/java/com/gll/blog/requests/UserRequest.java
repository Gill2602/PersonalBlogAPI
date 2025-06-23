package com.gll.blog.requests;

import com.gll.blog.entities.enums.Role;

import java.time.LocalDate;

public record UserRequest(
        Role role,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String email,
        String password
) {
}
