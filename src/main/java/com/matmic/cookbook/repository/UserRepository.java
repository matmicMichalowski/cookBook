package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName (String username);
}
