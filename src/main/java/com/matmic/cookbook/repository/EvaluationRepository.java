package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>{
}
