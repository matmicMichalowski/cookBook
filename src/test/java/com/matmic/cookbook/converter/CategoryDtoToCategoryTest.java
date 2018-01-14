package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryDtoToCategoryTest {
    public static final Long ID_VALUE = 1L;
    public static final String NAME = "european";

    private CategoryDtoToCategory converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoryDtoToCategory();
    }

    @Test
    public void convert() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(ID_VALUE);
        categoryDTO.setName(NAME);

        Category category = converter.convert(categoryDTO);

        assertNotNull(category);
        assertEquals(ID_VALUE, category.getId());
        assertEquals(NAME, category.getName());
    }

}