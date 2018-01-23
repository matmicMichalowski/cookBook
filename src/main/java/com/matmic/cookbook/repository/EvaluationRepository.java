package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>{

    @Query("SELECT e FROM evaluation AS e INNER JOIN e.rating rating" +
            " INNER JOIN e.user user WHERE rating.id=?#{[1]} AND user.id=?#{[0]}")
    Optional<Evaluation> findByIdAndUserId(Long userId, Long ratingId);
}
