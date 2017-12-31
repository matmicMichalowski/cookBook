package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByUserId(Long userID);
    List<Comment> findCommentsByRecipeId(Long recipeID);
}
