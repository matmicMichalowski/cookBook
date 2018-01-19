package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CommentDtoToComment;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Comment
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final CommentToCommentDto toCommentDto;
    private final CommentDtoToComment toComment;

    public CommentServiceImpl(CommentRepository commentRepository, CommentToCommentDto toCommentDto, CommentDtoToComment toComment) {
        this.commentRepository = commentRepository;

        this.toCommentDto = toCommentDto;
        this.toComment = toComment;
    }

    /**
     * Get all comments.
     *
     * @return the list of all entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllComments() {
        log.debug("Request to get all Comments");
        return commentRepository.findAll()
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get one by Comment id
     *
     * @param id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CommentDTO findCommentById(Long id) {
        log.debug("Request to get Comment by id: {}", id);
        Optional<Comment> optional = commentRepository.findById(id);
        if(optional.isPresent()){
            return toCommentDto.convert(optional.get());
        }
        return null;
    }

    /**
     * Get one by Comment id and User id
     *
     * @param userId id of User
     * @param commentId of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CommentDTO findUserCommentById(Long userId, Long commentId){
        log.debug("Request to get Comment by id: {} and User Id: {}", commentId, userId);
        return findCommentsByUser(userId).stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst().orElseThrow(NullPointerException::new);
    }

    /**
     * Get all comments by User.
     *
     * @param userId id of User
     * @return list of all User comments
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findCommentsByUser(Long userId) {
        log.debug("Request to get all User comments: {}", userId);
        return  commentRepository.findCommentsByUserId(userId)
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Get all comments by Recipe.
     *
     * @param recipeId id of Recipe
     * @return list of all Recipe comments
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findCommentByRecipe(Long recipeId) {
        log.debug("Request to get all comments from Recipe: {}", recipeId);
        return commentRepository.findCommentsByRecipeId(recipeId)
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Save a Comment
     *
     * @param commentDTO entity to be saved
     * @return the saved entity
     */
    @Override
    public CommentDTO saveOrUpdateComment(CommentDTO commentDTO) {
        log.debug("Request to save Comment: {}", commentDTO);
        Comment commentToSave = toComment.convert(commentDTO);
        Comment saved = commentRepository.save(commentToSave);
        return toCommentDto.convert(saved);
    }

    /**
     * Delete comment by id
     *
     * @param id the id of the entity
     */
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
