package com.matmic.cookbook.service;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.mapper.CommentMapper;
import com.matmic.cookbook.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }


    @Override
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> comments = new ArrayList<>();
        commentRepository.findAll().forEach(comment -> {
            comments.add(commentMapper.commentToCommentDto(comment));
        });
        return comments;
    }

    @Override
    public CommentDTO findCommentById(Long id) {
        Optional<Comment> optional = commentRepository.findById(id);
        if(optional.isPresent()){
            return commentMapper.commentToCommentDto(optional.get());
        }
        return null;
    }

    @Override
    public List<CommentDTO> findCommentsByUser(Long userId) {
        return  commentRepository.findCommentsByUserId(userId)
                .stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> findCommentByRecipe(Long recipeId) {
        return commentRepository.findCommentsByRecipeId(recipeId)
                .stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO saveOrUpdateComment(CommentDTO commentDTO) {
        Comment commentToSave = commentMapper.commentDtoToComment(commentDTO);
        commentRepository.save(commentToSave);
        return commentMapper.commentToCommentDto(commentToSave);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
