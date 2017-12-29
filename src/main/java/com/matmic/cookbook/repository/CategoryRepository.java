package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
