package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
