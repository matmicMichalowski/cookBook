package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CategoryDtoToCategory;
import com.matmic.cookbook.converter.CategoryToCategoryDto;
import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import com.matmic.cookbook.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {


    private CategoryDtoToCategory toCategory = new CategoryDtoToCategory();

    private CategoryToCategoryDto toCategoryDto = new CategoryToCategoryDto();

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryService = new CategoryServiceImpl(categoryRepository, toCategoryDto, toCategory);
    }

    @Test
    public void findAll() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Mexican");
        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("ameRican");

        categories.add(cat1);
        categories.add(cat2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> categoriesFound = categoryService.findAll();

        assertNotNull(categoriesFound);
        assertEquals(2, categoriesFound.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void saveCategory() throws Exception {
        Category newCategory = new Category();
        newCategory.setId(3L);
        newCategory.setName("spicy");

        CategoryDTO toBeSaved = new CategoryDTO();
        toBeSaved.setId(3L);
        toBeSaved.setName("spicy");

        when(categoryRepository.save(any())).thenReturn(newCategory);

        CategoryDTO savedCategory = categoryService.saveCategory(toBeSaved);

        assertEquals(Long.valueOf(3L), savedCategory.getId());
        assertNotNull(savedCategory);
        verify(categoryRepository,times(1)).save(any());

    }

    @Test
    public void findCategoryByName() throws Exception {
        Category category = new Category();
        category.setId(4L);
        category.setName("Fast Food");

        Optional<Category> findByName = Optional.of(category);

        when(categoryRepository.findCategoryByName(anyString())).thenReturn(findByName);

        CategoryDTO categoryFound = categoryService.findCategoryByName("");

        assertNotNull(categoryFound);
        assertEquals("fast food", categoryFound.getName());
    }

    @Test
    public void deleteCategory() throws Exception {
        Long toDelete = 2L;

        categoryService.deleteCategory(toDelete);

        verify(categoryRepository, times(1)).deleteById(anyLong());
        verify(categoryRepository, never()).deleteAll();
    }

}