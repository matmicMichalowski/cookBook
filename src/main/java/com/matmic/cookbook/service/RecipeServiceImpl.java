package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.RecipeDtoToRecipe;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
import com.matmic.cookbook.converter.UnitOfMeasureDtoToUnitOfMeasure;
import com.matmic.cookbook.domain.Ingredient;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final Logger log = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeDto toRecipeDto;
    private final RecipeDtoToRecipe toRecipe;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure;


    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeDto toRecipeDto, RecipeDtoToRecipe toRecipe,
                             CategoryService categoryService, UserService userService,
                             UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure) {
        this.recipeRepository = recipeRepository;
        this.toRecipeDto = toRecipeDto;
        this.toRecipe = toRecipe;
        this.categoryService = categoryService;
        this.userService = userService;
        this.toUnitOfMeasure = toUnitOfMeasure;
    }


    @Override
    public Page<RecipeDTO> findAllRecipes(Pageable pageable) {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAll(pageable).map(toRecipeDto::convert);
    }

    @Override
    public RecipeDTO findRecipeById(Long id) {
        log.debug("Request to get Recipe: {}", id);
        Optional<Recipe> optional = recipeRepository.findById(id);
        if(optional.isPresent()){
            return toRecipeDto.convert(optional.get());
        }
        log.debug("No Recipe with id: {}", id);
        return null;
    }

    @Override
    public List<RecipeDTO> findRecipeByCategory(String categoryName) {
        log.debug("Request to get all Recipes with given category: {}", categoryName);
        return recipeRepository.findAllByCategoryName(categoryName.toLowerCase()).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingValueBetweenLowAndHigh(int low, int high) {
        log.debug("Request to get all Recipes within rating value range: {}, {}", low, high);
        return recipeRepository.findAllRecipesBetweenRatingValues(low, high).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue) {
        log.debug("Request to get all Recipes with rating above: {}", aboveValue);
        return recipeRepository.findAllRecipesAboveRating(aboveValue).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue) {
        log.debug("Request to get all Recipes with rating below: {}", belowValue);
        return recipeRepository.findAllRecipesBelowRating(belowValue).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeDTO createNewRecipe(RecipeDTO recipeDTO, Long userId){
        log.debug("Request to create and save new Recipe: {}", recipeDTO);
        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipeDTO.getName());
        newRecipe.setDifficulty(recipeDTO.getDifficulty());
        newRecipe.setDirections(recipeDTO.getDirections());
        recipeDTO.getCategories().forEach(cat ->
                newRecipe.getCategories().add(categoryService.findByName(cat.getName())));
        newRecipe.setServings(recipeDTO.getServings());
        newRecipe.setCookTime(recipeDTO.getCookTime());
        recipeDTO.getIngredients().forEach(ingredientDTO -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientDTO.getName());
            ingredient.setAmount(ingredientDTO.getAmount());
            ingredient.setUnitOfMeasure(toUnitOfMeasure.convert(ingredientDTO.getUnitOfMeasure()));
            ingredient.setName(ingredientDTO.getName());
            ingredient.setRecipe(newRecipe);
            newRecipe.getIngredients().add(ingredient);
        });
        //Recipe newRecipe = toRecipe.convert(recipeDTO);
        User user = userService.findUserByID(userId);
        if ( user == null){
            return null;
        }
        newRecipe.setUser(user);
        newRecipe.setUserName(user.getName());
        Recipe savedRecipe = recipeRepository.save(newRecipe);

        return toRecipeDto.convert(savedRecipe);
    }

    @Override
    public RecipeDTO saveOrUpdateRecipe(RecipeDTO recipeDTO) {
        log.debug("Request to update and save Recipe: {}", recipeDTO);
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
        log.debug("Request to delete Recipe: {}", id);
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
