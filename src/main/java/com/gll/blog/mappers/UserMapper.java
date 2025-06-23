package com.gll.blog.mappers;

import com.gll.blog.entities.UserEntity;
import com.gll.blog.responses.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateBirth()
        );
    }
}
