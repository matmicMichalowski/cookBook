package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String>{
    Authority findOneByName(String string);
}
