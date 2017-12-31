package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.mapper.RecipeMapper;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByUser(String username) {
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByCategory(String categoryName) {
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingValue(double low, double high) {
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingAboveValue(double aboveValue) {
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingBelowValue(double belowValue) {
        return null;
    }

    @Override
    public RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO) {
        return null;
    }

    @Override
    public void deleteRecipe(Long id) {

    }
}
