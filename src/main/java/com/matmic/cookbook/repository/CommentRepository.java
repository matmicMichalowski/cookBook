package com.matmic.cookbook.repository;

import com.matmic.cookbook.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
