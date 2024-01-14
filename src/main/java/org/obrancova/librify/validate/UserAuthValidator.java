package org.obrancova.librify.validate;

import lombok.RequiredArgsConstructor;
import org.obrancova.librify.exception.DataValidationException;
import org.obrancova.librify.model.user.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthValidator {
    public void validate(User user) {
        validateNotNull(user.getEmail(), "Email is required");
        validateNotNull(user.getPassword(), "Password is required");
        validatePasswordLength(user.getPassword());
        validateEmailFormat(user.getEmail());
    }

    private static void validateNotNull(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new DataValidationException(errorMessage);
        }
    }

    private static void validatePasswordLength(String password) {
        if (password.length() < 8) {
            throw new DataValidationException("Password must be at least 8 characters long");
        }
    }

    private static void validateEmailFormat(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailPattern)) {
            throw new DataValidationException("Invalid email format");
        }
    }

}
