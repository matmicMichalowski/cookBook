package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByName (String username);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = "user")
    Optional<User> findOneWithAuthoritiesByName(String username);

}
