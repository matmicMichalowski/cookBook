package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

   @Query("select r from recipe as r inner join r.rating where total_Rating >= ?#{[0]}")
   List<Recipe> findAllRecipesAboveRating(int aboveValue);
}
