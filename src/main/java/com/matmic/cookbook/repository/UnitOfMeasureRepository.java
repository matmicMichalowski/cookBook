package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, Long>{
    Optional<UnitOfMeasure> findById(Long id);
}
