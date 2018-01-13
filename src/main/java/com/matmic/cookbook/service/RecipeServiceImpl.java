package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.RecipeDtoToRecipe;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.repository.RecipeRepository;
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
    private final RecipeToRecipeDto toRecipeDto;
    private final RecipeDtoToRecipe toRecipe;
    private final UserService userService;


    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeDto toRecipeDto, RecipeDtoToRecipe toRecipe, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.toRecipeDto = toRecipeDto;
        this.toRecipe = toRecipe;
        this.userService = userService;
    }


    @Override
    public Page<RecipeDTO> findAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(toRecipeDto::convert);
    }

    @Override
    public RecipeDTO findRecipeById(Long id) {
        Optional<Recipe> optional = recipeRepository.findById(id);
        if(optional.isPresent()){
            return toRecipeDto.convert(optional.get());
        }
        return null;
    }


    @Override
    public List<RecipeDTO> findRecipeByCategory(String categoryName) {
        List<RecipeDTO> recipes = new ArrayList<>();
        recipeRepository.findAll().forEach(recipe -> {
            recipe.getCategories().forEach(category -> {
                if (category.getName().equals(categoryName)){
                    recipes.add(toRecipeDto.convert(recipe));
                }
            });
        });

        return recipes;
    }


    @Override
    public List<RecipeDTO> findRecipeByRatingValue(int low, int high) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() >= low && recipe.getRating().getTotalRating() <= high)
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() >= aboveValue)
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue) {
        return recipeRepository.findAll().stream()
                .filter(recipe -> recipe.getRating().getTotalRating() <= belowValue)
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO createNewRecipe(RecipeDTO recipeDTO, Long userId){
        Recipe recipe = toRecipe.convert(recipeDTO);
        User user = userService.findUserByID(userId);
        if ( user == null){
            return null;
        }
        recipe.setUser(user);
        Recipe savedRecipe = recipeRepository.save(recipe);

        return toRecipeDto.convert(savedRecipe);
    }

    @Override
    public RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO) {
        Recipe detachedRecipe = toRecipe.convert(recipeDTO);
        Optional<Recipe> optional = recipeRepository.findById(recipeDTO.getId());

        if (optional.isPresent()) {
            Recipe savedRecipe = recipeRepository.save(detachedRecipe);
            return toRecipeDto.convert(savedRecipe);
        }

        Rating rating = new Rating();
        rating.setRecipe(detachedRecipe);
        detachedRecipe.setRating(rating);
        recipeRepository.save(detachedRecipe);
        return toRecipeDto.convert(detachedRecipe);
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
