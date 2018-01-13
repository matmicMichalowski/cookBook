package com.matmic.cookbook;

import com.matmic.cookbook.converter.CategoryDtoToCategory;
import com.matmic.cookbook.domain.*;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.repository.*;
import com.matmic.cookbook.service.CategoryService;
import com.matmic.cookbook.service.IngredientService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent>{
    private final CategoryService categoryService;
    private final IngredientService ingredientService;
    private final CategoryDtoToCategory categoryConverter;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public Bootstrap(CategoryService categoryService, IngredientService ingredientService, CategoryDtoToCategory categoryConverter, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, UnitOfMeasureRepository unitOfMeasureRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
        this.categoryConverter = categoryConverter;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createUom();
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setName("Sweet");
        CategoryDTO saved = categoryService.saveCategory(newCategory);

        Category category = categoryConverter.convert(newCategory);

        CategoryDTO found = categoryService.findCategoryByName("Sweet");



        //commentRepository.save(comment);

        Optional<UnitOfMeasure> optional = unitOfMeasureRepository.findById(1L);
        UnitOfMeasure unit = new UnitOfMeasure();
        if (optional.isPresent()){
            unit = optional.get();
        }

        User user = new User();
        user.setName("UserTest");
        user.setEmail("mail@test.com");
        user.setPassword("pass");


        userRepository.save(user);

        Recipe recipe = new Recipe();
        //recipe.setId(2L);
        Rating rating = new Rating();
        rating.setRecipe(recipe);
        rating.setTotalRating(5.0);
        recipe.setRating(rating);
        recipe.setUser(user);
        user.getRecipes().add(recipe);

        Ingredient ingredient = new Ingredient();
        //ingredient.setId(2L);
        ingredient.setUnitOfMeasure(unit);
        ingredient.setRecipe(recipe);
        ingredient.setName("Pepper");
        ingredient.setAmount(1);

        recipe.getIngredients().add(ingredient);

        recipeRepository.save(recipe);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.getUser().getComments().add(comment);
        comment.setRecipe(recipe);
        comment.getRecipe().getComments().add(comment);
        comment.setComment("Test Comment");
        commentRepository.saveAndFlush(comment);




        System.out.println(found.getName());
        System.out.println(category.getName());
        System.out.println(saved.getName());
    }

    public void  createUom(){
        UnitOfMeasure unit1 = new UnitOfMeasure();
        unit1.setName("Pinch");
        unitOfMeasureRepository.save(unit1);
        UnitOfMeasure unit2 = new UnitOfMeasure();
        unit2.setName("Tablespoon");
        unitOfMeasureRepository.save(unit2);
        UnitOfMeasure unit3 = new UnitOfMeasure();
        unit3.setName("Teaspoon");
        unitOfMeasureRepository.save(unit3);
        UnitOfMeasure unit4 = new UnitOfMeasure();
        unit4.setName("Drop");
        unitOfMeasureRepository.save(unit4);
        UnitOfMeasure unit5 = new UnitOfMeasure();
        unit5.setName("Milliliter");
        unitOfMeasureRepository.save(unit5);
        UnitOfMeasure unit6 = new UnitOfMeasure();
        unit6.setName("Cup");
        unitOfMeasureRepository.save(unit6);
    }
}
