package com.matmic.cookbook.service;

import com.matmic.cookbook.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();
    CommentDTO findCommentById(Long id);
    List<CommentDTO> findCommentsByUser(Long userId);
    List<CommentDTO> findCommentByRecipe(Long recipeId);
    CommentDTO saveOrUpdateComment(CommentDTO commentDTO);
    void deleteComment(Long id);
}
