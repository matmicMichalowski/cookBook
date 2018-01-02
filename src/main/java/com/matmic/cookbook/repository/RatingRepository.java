package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRecipe(Recipe recipe);
}
