package org.obrancova.librify.controller;

import lombok.RequiredArgsConstructor;
import org.obrancova.librify.exception.AuthenticateException;
import org.obrancova.librify.exception.DataValidationException;
import org.obrancova.librify.exception.RegisterException;
import org.obrancova.librify.model.user.User;
import org.obrancova.librify.service.AuthService;
import org.owasp.encoder.Encode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowedOrigins}")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            String cleanedEmail = Encode.forHtml(user.getEmail());
            String cleanedPassword = Encode.forHtml(user.getPassword());

            User registeredUser = authService.registerUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (DataValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: " + e.getMessage());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with email " + user.getEmail() + " already exists. Try another");
        } catch (RegisterException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginUser) {
        try {
            String cleanedEmail = Encode.forHtml(loginUser.getEmail());
            String cleanedPassword = Encode.forHtml(loginUser.getPassword());

            String token = authService.authenticateUser(cleanedEmail, cleanedPassword);

            return ResponseEntity.ok(token);
        } catch (AuthenticateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("The password is incorrect. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

}