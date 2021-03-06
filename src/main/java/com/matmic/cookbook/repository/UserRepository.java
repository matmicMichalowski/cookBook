package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName (String username);
    Optional<User> findByEmailOrName(String email, String name);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByName(String username);
    Optional<User> findOneByActivationToken(String activationToken);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByResetToken(String resetToken);
}
