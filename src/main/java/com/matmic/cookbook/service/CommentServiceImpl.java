package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CommentDtoToComment;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
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
    private final CommentToCommentDto toCommentDto;
    private final CommentDtoToComment toComment;

    public CommentServiceImpl(CommentRepository commentRepository, CommentToCommentDto toCommentDto, CommentDtoToComment toComment) {
        this.commentRepository = commentRepository;

        this.toCommentDto = toCommentDto;
        this.toComment = toComment;
    }


    @Override
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> comments = new ArrayList<>();
        commentRepository.findAll().forEach(comment -> {
            comments.add(toCommentDto.convert(comment));
        });
        return comments;
    }

    @Override
    public CommentDTO findCommentById(Long id) {
        Optional<Comment> optional = commentRepository.findById(id);
        if(optional.isPresent()){
            return toCommentDto.convert(optional.get());
        }
        return null;
    }

    @Override
    public CommentDTO findUserCommentById(Long userId, Long commentId){
        return findCommentsByUser(userId).stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    @Override
    public List<CommentDTO> findCommentsByUser(Long userId) {
        return  commentRepository.findCommentsByUserId(userId)
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> findCommentByRecipe(Long recipeId) {
        return commentRepository.findCommentsByRecipeId(recipeId)
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO saveOrUpdateComment(CommentDTO commentDTO) {
        Comment commentToSave = toComment.convert(commentDTO);
        Comment saved = commentRepository.save(commentToSave);
        return toCommentDto.convert(saved);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
