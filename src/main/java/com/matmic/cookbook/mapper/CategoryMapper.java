package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Category;
import com.matmic.cookbook.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDto(Category category);

    Category categoryDtoToCategory(CategoryDTO categoryDTO);
}
