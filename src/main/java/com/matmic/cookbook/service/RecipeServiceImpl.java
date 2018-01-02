package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.mapper.RecipeMapper;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userRepository = userRepository;
    }


    @Override
    public Page<RecipeDTO> allRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(recipeMapper::recipeToRecipeDto);
    }

    @Override
    public RecipeDTO findRecipeById(Long id) {
        Optional<Recipe> optional = recipeRepository.findById(id);
        if(optional.isPresent()){
            return recipeMapper.recipeToRecipeDto(optional.get());
        }
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByUser(String username) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getUser().getName().equals(username))
                .map(recipeMapper::recipeToRecipeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByCategory(Category category) {
        List<RecipeDTO> recipes = new ArrayList<>();

        recipeRepository.findAll().forEach(recipe -> {
            if (recipe.getCategories().contains(category)){
                recipes.add(recipeMapper.recipeToRecipeDto(recipe));
            }
        });

        return recipes;
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingValue(int low, int high) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() >= low && recipe.getRating().getTotalRating() <= high)
                .map(recipeMapper::recipeToRecipeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() >= aboveValue)
                .map(recipeMapper::recipeToRecipeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() <= belowValue)
                .map(recipeMapper::recipeToRecipeDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO) {
        Recipe detachedRecipe = recipeMapper.recipeDtoToRecipe(recipeDTO);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        return recipeMapper.recipeToRecipeDto(savedRecipe);
    }

    @Override
    public void deleteRecipe(Long id) {
        Optional<Recipe> optional = recipeRepository.findById(id);

        if (optional.isPresent()){
            Recipe recipe = optional.get();
            User user = recipe.getUser();
            user.getRecipes().remove(recipe);
            recipe.setUser(null);
            recipeRepository.delete(recipe);
        }
    }
}
