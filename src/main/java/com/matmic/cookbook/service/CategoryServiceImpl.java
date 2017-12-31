package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        Category newCategory = categoryMapper.categoryDtoToCategory(categoryDTO);
        categoryRepository.save(newCategory);
        return categoryMapper.categoryToCategoryDto(newCategory);
    }

    @Override
    public CategoryDTO findCategoryByName(String name) {
        Optional<Category> optional = categoryRepository.findCategoryByName(name);
        if(optional.isPresent()){
            return categoryMapper.categoryToCategoryDto(optional.get());
        }
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);

    }
}
