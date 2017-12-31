package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.dto.RatingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RatingMapper {

    RatingMapper INSTANCE = Mappers.getMapper(RatingMapper.class);

    RatingDTO ratingToRatingDto(Rating rating);

    Rating ratingDtoToRating(RatingDTO ratingDTO);

}
