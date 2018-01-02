package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.RatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    @Mapping(source = "id", target = "id")
    RatingDTO ratingToRatingDto(Rating rating);

    @Mapping(source = "id", target = "id")
    Rating ratingDtoToRating(RatingDTO ratingDTO);

}
