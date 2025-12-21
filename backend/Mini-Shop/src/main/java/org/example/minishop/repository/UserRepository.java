package org.example.minishop.repository;

import jakarta.transaction.Transactional;
import org.example.minishop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);

    User getUserByUsername(String username);
}
