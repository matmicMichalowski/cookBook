package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class CategoryToCategoryDto implements Converter<Category, CategoryDTO>{

    @Synchronized
    @Nullable
    @Override
    public CategoryDTO convert(Category category) {
        if (category == null) {
            return null;
        }

        final CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }
}
