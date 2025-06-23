package com.gll.blog.security;

import com.gll.blog.entities.UserEntity;
import com.gll.blog.exceptions.NotFoundException;
import com.gll.blog.repositories.UserRepository;
import com.gll.blog.requests.AuthRequest;
import com.gll.blog.responses.AuthResponse;
import com.gll.blog.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String jwtToken;
        UserResponse userResponse;
        if (authentication.isAuthenticated()) {
            log.info("Authentication successful by user {}", request.email());
            jwtToken = jwtService.generateToken(request.email());

            UserEntity userEntity = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new NotFoundException("Not found user with email: " + request.email()));

            userResponse = new UserResponse(
                    userEntity.getId(),
                    userEntity.getRole(),
                    userEntity.getEmail(),
                    userEntity.getFirstName(),
                    userEntity.getLastName(),
                    userEntity.getDateBirth()
            );
        }
        else {
            log.info("Authentication failed by user {}", request.email());
            jwtToken = "Authentication failed";

            userResponse = null;
        }

        return new AuthResponse(jwtToken, userResponse);
    }
}
