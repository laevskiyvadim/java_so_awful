package org.example.avtodiller.repositories;

import org.example.avtodiller.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    @Query(value = "SELECT * FROM users u WHERE u.login = :login", nativeQuery = true)
    UserModel findByLogin(String login);
}
