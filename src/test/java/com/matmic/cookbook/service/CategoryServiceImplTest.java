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

    @Mock
    private CategoryDtoToCategory categoryConv;

    @Mock
    private CategoryToCategoryDto categoryConvDto;

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService service;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        service = new CategoryServiceImpl(categoryRepository, categoryConvDto, categoryConv);
    }

    @Test
    public void findAll() throws Exception {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category());
        categories.add(new Category());

        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> categoriesFound = service.findAll();

        assertNotNull(categoriesFound);
        assertEquals(2, categoriesFound.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void saveCategory() throws Exception {
        Category newCategory = new Category();
        newCategory.setId(3L);
        newCategory.setName("Spicy");

        CategoryDTO toBeSaved = new CategoryDTO();
        toBeSaved.setId(3L);
        toBeSaved.setName("Spicy");

        when(categoryConv.convert(any())).thenReturn(newCategory);
        when(categoryRepository.save(any())).thenReturn(newCategory);
        when(categoryConvDto.convert(any())).thenReturn(toBeSaved);

        CategoryDTO savedCategory = service.saveCategory(new CategoryDTO());

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

        CategoryDTO categoryFound = service.findCategoryByName("");

        System.out.println(categoryFound.getName());

        assertNotNull(categoryFound);
    }

    @Test
    public void deleteCategory() throws Exception {
    }

}