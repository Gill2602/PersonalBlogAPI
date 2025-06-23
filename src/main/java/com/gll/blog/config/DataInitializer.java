package com.gll.blog.config;

import com.gll.blog.entities.UserEntity;
import com.gll.blog.entities.enums.Role;
import com.gll.blog.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;


@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDefaultUser(final UserRepository userRepository,
                                             final PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByRole(Role.ADMIN)) {
                UserEntity user = UserEntity.builder()
                        .role(Role.ADMIN)
                        .email("admin.user@blog.it")
                        .password(passwordEncoder.encode("Administrator-User"))
                        .firstName("Admin")
                        .lastName("User")
                        .dateBirth(LocalDate.of(2009, 2, 26))
                        .build();
                userRepository.save(user);

                log.info("created initializer user: {} with role ADMIN", user.getEmail());
            }
        };
    }
}

