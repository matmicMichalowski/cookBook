package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
