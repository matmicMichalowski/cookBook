package com.matmic.cookbook;

import com.matmic.cookbook.converter.CategoryDtoToCategory;
import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.service.CategoryService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent>{
    private final CategoryService categoryService;
    private final CategoryDtoToCategory categoryConverter;

    public Bootstrap(CategoryService categoryService, CategoryDtoToCategory categoryConverter) {
        this.categoryService = categoryService;
        this.categoryConverter = categoryConverter;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setName("Sweet");
        CategoryDTO saved = categoryService.saveCategory(newCategory);

        Category category = categoryConverter.convert(newCategory);

        CategoryDTO found = categoryService.findCategoryByName("Sweet");

        System.out.println(found.getName());
        System.out.println(category.getName());
        System.out.println(saved.getName());
    }
}
