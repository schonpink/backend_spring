package org.obrancova.librify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.obrancova.librify.exception.AuthenticateException;
import org.obrancova.librify.model.user.User;
import org.obrancova.librify.repository.AuthRepositoryImpl;
import org.obrancova.librify.service.secure.JwtTokenUtil;
import org.obrancova.librify.service.secure.PasswordHasher;
import org.obrancova.librify.validate.UserAuthValidator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepositoryImpl authRepositoryImpl;
    private final UserAuthValidator userAuthValidator;
    private final PasswordHasher passwordHasher;
    private final JwtTokenUtil jwtTokenUtil;

    public User registerUser(User user) {
        log.info("Creating a new user: {}", user);

        userAuthValidator.validate(user);

        String hashedPassword = passwordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        User registeredUser = authRepositoryImpl.createUser(user);

        log.info("User registered successfully: {}", registeredUser);
        return registeredUser;
    }

    public String authenticateUser(String email, String password) {
        log.info("Authenticating user: {}", email);

        User user = authRepositoryImpl.getUserByEmail(email);

        if (user != null && passwordHasher.checkPassword(password, user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user);
            log.info("User authenticated successfully: {}", email);
            return token;
        } else {
            log.error("Authentication failed for user: {}", email);
            throw new AuthenticateException("Invalid username or password");
        }
    }

}