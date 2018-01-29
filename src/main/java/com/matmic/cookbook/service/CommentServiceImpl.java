package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CommentDtoToComment;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    private final CommentRepository commentRepository;
    private final CommentToCommentDto toCommentDto;
    private final CommentDtoToComment toComment;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentToCommentDto toCommentDto,
                              CommentDtoToComment toComment, UserRepository userRepository,
                              RecipeRepository recipeRepository) {

        this.commentRepository = commentRepository;
        this.toCommentDto = toCommentDto;
        this.toComment = toComment;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Get all comments.
     *
     * @param pageable pagination information
     * @return the list of all entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentDTO> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable).map(toCommentDto::convert);
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
        return commentRepository.findCommentsByRecipeId(recipeId)
                .stream()
                .map(toCommentDto::convert)
                .collect(Collectors.toList());
    }

    /**
     * Create new Comment
     *
     * @param commentDTO comment to be saved
     * @return optional of saved comment
     */
    @Override
    public Optional<CommentDTO> saveNewComment(CommentDTO commentDTO){
        Optional<User> userOptional = userRepository.findById(commentDTO.getUserId());
        Optional<Recipe> recipeOptional = recipeRepository.findById(commentDTO.getRecipeId());
        if (userOptional.isPresent() && recipeOptional.isPresent()){
            User user = userOptional.get();
            Recipe recipe = recipeOptional.get();

            Comment newComment = toComment.convert(commentDTO);
            newComment.setUser(user);
            newComment.setRecipe(recipe);
            Comment savedComment = commentRepository.save(newComment);

            return Optional.of(toCommentDto.convert(savedComment));
        }
        return Optional.empty();
    }

    /**
     * Update a Comment
     *
     * @param commentDTO entity to be saved
     * @return the saved entity
     */
    @Override
    public CommentDTO saveOrUpdateComment(CommentDTO commentDTO) {
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
