package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
