package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Page<CommentDTO> getAllComments(Pageable pageable);
    CommentDTO findCommentById(Long id);
    List<CommentDTO> findCommentsByUser(Long userId);
    CommentDTO findUserCommentById(Long userId, Long commentId);
    Optional<CommentDTO> saveNewComment(CommentDTO commentDTO);
    List<CommentDTO> findCommentByRecipe(Long recipeId);
    CommentDTO saveOrUpdateComment(CommentDTO commentDTO);
    void deleteComment(Long id);
}
