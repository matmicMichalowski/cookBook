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

/**
 * Service Implementation for managing Recipe
 */
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

    /**
     * Get all recipes
     *
     * @param pageable the pagination information
     * @return list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RecipeDTO> findAllRecipes(Pageable pageable) {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAll(pageable).map(toRecipeDto::convert);
    }

    /**
     * Get one recipe by id
     *
     * @param id the id of entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RecipeDTO findRecipeById(Long id) {
        log.debug("Request to get Recipe: {}", id);
        Optional<Recipe> optional = recipeRepository.findById(id);
        if(optional.isPresent()){
            return toRecipeDto.convert(optional.get());
        }
        log.debug("No Recipe with id: {}", id);
        return null;
    }

    /**
     * Get recipes by category name
     *
     * @param categoryName the name of category entity
     * @return list of all recipes by category
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> findRecipeByCategory(String categoryName) {
        log.debug("Request to get all Recipes with given category: {}", categoryName);
        return recipeRepository.findAllByCategoryName(categoryName.toLowerCase()).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get recipes between given rating value
     *
     * @param low rating low value
     * @param high rating high value
     * @return list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> findRecipeByRatingValueBetweenLowAndHigh(int low, int high) {
        log.debug("Request to get all Recipes within rating value range: {}, {}", low, high);
        return recipeRepository.findAllRecipesBetweenRatingValues(low, high).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get recipes above given value
     *
     * @param aboveValue an indicator above which recipes will be found
     * @return list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> findRecipeByRatingAboveValue(int aboveValue) {
        log.debug("Request to get all Recipes with rating above: {}", aboveValue);
        return recipeRepository.findAllRecipesAboveRating(aboveValue).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get recipes below given value
     *
     * @param belowValue an indicator below which recipes will be found
     * @return list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RecipeDTO> findRecipeByRatingBelowValue(int belowValue) {
        log.debug("Request to get all Recipes with rating below: {}", belowValue);
        return recipeRepository.findAllRecipesBelowRating(belowValue).stream()
                .map(toRecipeDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Create and save new Recipe entity
     *
     * @param recipeDTO entity to be saved
     * @param userId id of user who post recipe
     * @return saved entity
     */
    @Override
    public RecipeDTO createNewRecipe(RecipeDTO recipeDTO, Long userId){
        log.debug("Request to create and save new Recipe: {}", recipeDTO);
        User user = userService.findUserByID(userId);
        if ( user == null){
            return null;
        }
        Recipe newRecipe = new Recipe();
        newRecipe.setUser(user);
        newRecipe.setUserName(user.getName());
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

        Recipe savedRecipe = recipeRepository.save(newRecipe);

        return toRecipeDto.convert(savedRecipe);
    }

    /**
     * Save and update Recipe entity
     *
     * @param recipeDTO entity to be updated
     * @return saved and updated entity
     */
    @Override
    public RecipeDTO saveAndUpdateRecipe(RecipeDTO recipeDTO) {
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

    /**
     * Delete Recipe by entity id
     *
     * @param id entity id
     */
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
