package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CategoryDtoToCategory;
import com.matmic.cookbook.converter.CategoryToCategoryDto;
import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Category
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDto toCategoryDto;
    private final CategoryDtoToCategory toCategory;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryDto categoryConvDto, CategoryDtoToCategory categoryConv) {
        this.categoryRepository = categoryRepository;
        this.toCategoryDto = categoryConvDto;
        this.toCategory = categoryConv;
    }

    /**
     * Get all categories.
     *
     * @return the list of all entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(toCategoryDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Save a category.
     *
     * @param categoryDTO entity to be saved
     * @return the saved entity
     */
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category detachedCategory = toCategory.convert(categoryDTO);
        detachedCategory.setName(categoryDTO.getName().toLowerCase());
        Category savedCategory = categoryRepository.save(detachedCategory);

        return toCategoryDto.convert(savedCategory);
    }

    /**
     * Get one by Category name
     *
     * @param name of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Category findByName(String name){
        Optional<Category> categoryOptional = categoryRepository.findCategoryByName(name);
        if (categoryOptional.isPresent()){
            return categoryOptional.get();
        }
        return null;
    }

    /**
     *Get one CategoryDTO by name
     *
     * @param name of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findCategoryByName(String name) {
        return toCategoryDto.convert(findByName(name));
    }

    /**
     * Delete the category by id
     *
     * @param id the id of the entity
     */
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
