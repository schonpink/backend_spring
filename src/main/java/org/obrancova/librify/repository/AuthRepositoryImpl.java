package org.obrancova.librify.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.obrancova.librify.exception.RegisterException;
import org.obrancova.librify.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
    @Value("${auth.file.path}")
    private String authFilePath;

    public User createUser(User user) {
        List<User> users = loadUsersFromFile(authFilePath);
        if (users.stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            log.error("User with email {} already exists", user.getEmail());
            throw new RegisterException("User with email " + user.getEmail() + " already exists");
        }

        user.setId(generateNewId(users));

        users.add(user);

        saveUsersToFile(users, authFilePath);
        return user;
    }

    public User getUserByEmail(String email) {
        List<User> users = loadUsersFromFile(authFilePath);
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
    }

    private static List<User> loadUsersFromFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<User>());
            }

            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void saveUsersToFile(List<User> users, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            File file = new File(fileName);
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long generateNewId(List<User> users) {
        return users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
    }

}