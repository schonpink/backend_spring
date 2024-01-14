package org.obrancova.librify.repository;

import org.obrancova.librify.model.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {

    User createUser(User user);

    User getUserByEmail(String email);

}