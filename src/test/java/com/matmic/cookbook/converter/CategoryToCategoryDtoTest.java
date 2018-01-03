package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryToCategoryDtoTest {
    public static final Long ID_VALUE = 3L;
    public static final String NAME = "Mexican";

    private CategoryToCategoryDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoryToCategoryDto();
    }

    @Test
    public void convert() throws Exception {
        Category category = new Category();
        category.setId(ID_VALUE);
        category.setName(NAME);

        CategoryDTO categoryDTO = converter.convert(category);

        assertNotNull(categoryDTO);
        assertEquals(ID_VALUE, categoryDTO.getId());
        assertEquals(NAME, categoryDTO.getName());
    }

}