package com.gll.blog.services;

import com.gll.blog.entities.UserEntity;
import com.gll.blog.entities.enums.Role;
import com.gll.blog.exceptions.DataValidityException;
import com.gll.blog.exceptions.NotFoundException;
import com.gll.blog.repositories.UserRepository;
import com.gll.blog.requests.UserRequest;
import com.gll.blog.responses.UserResponse;
import com.gll.blog.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerStandardUser(UserRequest request) {

        checkRegistrationDataValidity(request);

        UserEntity user = UserEntity.builder()
                .role(Role.USER)
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .dateBirth(request.birthDate())
                .build();

        UserEntity savedUser = userRepository.save(user);
        log.info("created new standard user with email: {} and ID: {}",
                savedUser.getEmail(), savedUser.getId()
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getRole(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getDateBirth()
        );
    }

    public Page<UserResponse> readAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(savedUser -> new UserResponse(
                        savedUser.getId(),
                        savedUser.getRole(),
                        savedUser.getEmail(),
                        savedUser.getFirstName(),
                        savedUser.getLastName(),
                        savedUser.getDateBirth()
                ));
    }

    public UserResponse update(UUID id, UserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("not found user with ID: " + id));

        if (AppUtils.isNotBlank(request.firstName())) {
            user.setFirstName(request.firstName());
        }
        if (AppUtils.isNotBlank(request.lastName())) {
            user.setLastName(request.lastName());
        }
        if (request.birthDate() != null) {
            user.setDateBirth(request.birthDate());
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }

        UserEntity updatedUser = userRepository.save(user);
        log.info("updated user with email: {}, role: {} and ID: {}",
                updatedUser.getEmail(), updatedUser.getRole(), updatedUser.getId()
        );

        return new UserResponse(
                updatedUser.getId(),
                updatedUser.getRole(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getDateBirth()
        );
    }

    public void deleteById(UUID id) {
        UserEntity user = userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("not found user with ID: " + id));

        log.info("deleted user with email: {} and ID: {}",
                user.getEmail(), user.getId()
        );

        userRepository.delete(user);
    }

    private void checkRegistrationDataValidity(UserRequest request) {

        if (AppUtils.isBlank(request.email()) || userRepository.existsByEmail(request.email())) {
            throw new DataValidityException("The user email is invalid or already used");
        }

        if (AppUtils.isBlank(request.password()) || request.password().length() < 8) {
            throw new DataValidityException("The password is invalid or too short (minimum 8 characters required).");
        }

        if (AppUtils.isBlank(request.firstName()) || AppUtils.isBlank(request.lastName())) {
            throw new DataValidityException("The first name or last name is invalid.");
        }

        if (request.birthDate() == null || (Period.between(request.birthDate(),LocalDate.now()).getYears()) < 14) {
            throw new DataValidityException("Invalid date of birth or age must be at least 14 years.");
        }
    }
}
