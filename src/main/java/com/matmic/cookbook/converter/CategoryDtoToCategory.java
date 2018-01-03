package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToCategory implements Converter<CategoryDTO, Category> {

    @Synchronized
    @Nullable
    @Override
    public Category convert(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        final Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        return category;
    }
}
