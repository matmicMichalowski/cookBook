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

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDto categoryConvDto;
    private final CategoryDtoToCategory categoryConv;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryDto categoryConvDto, CategoryDtoToCategory categoryConv) {
        this.categoryRepository = categoryRepository;
        this.categoryConvDto = categoryConvDto;
        this.categoryConv = categoryConv;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryConvDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category detachedCategory = categoryConv.convert(categoryDTO);
        detachedCategory.setName(categoryDTO.getName().toLowerCase());
        Category savedCategory = categoryRepository.save(detachedCategory);

        return categoryConvDto.convert(savedCategory);
    }

    @Override
    public Category findByName(String name){
        Optional<Category> categoryOptional = categoryRepository.findCategoryByName(name);
        if (categoryOptional.isPresent()){
            return categoryOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public CategoryDTO findCategoryByName(String name) {

        return categoryConvDto.convert(findByName(name));
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
