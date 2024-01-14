package org.obrancova.librify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.obrancova.librify.exception.AuthenticateException;
import org.obrancova.librify.model.user.User;
import org.obrancova.librify.repository.AuthRepositoryImpl;
import org.obrancova.librify.service.secure.JwtTokenUtil;
import org.obrancova.librify.service.secure.PasswordHasher;
import org.obrancova.librify.validate.UserAuthValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthRepositoryImpl authRepositoryImpl;

    @Mock
    private UserAuthValidator userAuthValidator;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("example@example.com")
                .password("password123")
                .build();

        authService = new AuthService(authRepositoryImpl, userAuthValidator, passwordHasher, jwtTokenUtil);
    }

    @Test
    void testRegisterUser() {

        when(passwordHasher.hashPassword(user.getPassword())).thenReturn("hashedPassword");
        when(authRepositoryImpl.createUser(user)).thenReturn(user);

        User registeredUser = authService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("hashedPassword", registeredUser.getPassword());
        verify(userAuthValidator, times(1)).validate(user);
        verify(authRepositoryImpl, times(1)).createUser(user);
    }

    @Test
    void testAuthenticateUser() {
        String email = "test@example.com";
        String password = "password";

        when(authRepositoryImpl.getUserByEmail(email)).thenReturn(user);
        when(passwordHasher.checkPassword(password, user.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(user)).thenReturn("token");

        String token = authService.authenticateUser(email, password);

        assertNotNull(token);
        assertEquals("token", token);
        verify(authRepositoryImpl, times(1)).getUserByEmail(email);
        verify(passwordHasher, times(1)).checkPassword(password, user.getPassword());
        verify(jwtTokenUtil, times(1)).generateToken(user);
    }

    @Test
    void testAuthenticateUserInvalidPassword() {
        String email = "test@example.com";
        String password = "invalidPassword";

        when(authRepositoryImpl.getUserByEmail(email)).thenReturn(user);
        when(passwordHasher.checkPassword(password, user.getPassword())).thenReturn(false);

        assertThrows(AuthenticateException.class, () -> authService.authenticateUser(email, password));
        verify(authRepositoryImpl, times(1)).getUserByEmail(email);
        verify(passwordHasher, times(1)).checkPassword(password, user.getPassword());
        verify(jwtTokenUtil, never()).generateToken(user);
    }

}