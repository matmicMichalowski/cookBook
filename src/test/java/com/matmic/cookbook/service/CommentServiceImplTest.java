package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.CommentDtoToComment;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentToCommentDto toCommentDto = new CommentToCommentDto();

    private CommentDtoToComment toComment = new CommentDtoToComment();

    private CommentService commentService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        commentService = new CommentServiceImpl(commentRepository, toCommentDto, toComment);
    }

    @Test
    public void getAllComments() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(4L);

        User user = new User();
        user.setId(5L);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setRecipe(recipe);
        recipe.getComments().add(comment1);
        comment1.setUser(user);
        user.getComments().add(comment1);


        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setRecipe(recipe);
        recipe.getComments().add(comment2);
        comment2.setUser(user);
        user.getComments().add(comment2);


        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        when(commentRepository.findAll()).thenReturn(comments);

        List<CommentDTO> commentDTOList = commentService.getAllComments();

        assertNotNull(commentDTOList);
        assertEquals(2, commentDTOList.size());
    }


    @Test
    public void findCommentsByUser() throws Exception {
        User user = new User();
        user.setId(1L);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setRecipe(new Recipe());
        comment.setUser(user);
        user.getComments().add(comment);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findCommentsByUserId(anyLong())).thenReturn(comments);

        List<CommentDTO> commentsFound = commentService.findCommentsByUser(1L);

        assertNotNull(commentsFound);
        assertEquals(1, commentsFound.size());
        verify(commentRepository, times(1)).findCommentsByUserId(anyLong());
    }

    @Test
    public void findCommentByRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setRecipe(recipe);
        recipe.getComments().add(comment);
        comment.setUser(new User());

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findCommentsByRecipeId(anyLong())).thenReturn(comments);

        List<CommentDTO> commentsFound = commentService.findCommentByRecipe(1L);

        assertNotNull(commentsFound);
        assertEquals(1, commentsFound.size());
        verify(commentRepository, times(1)).findCommentsByRecipeId(anyLong());
    }

    @Test
    public void saveOrUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setRecipe(new Recipe());
        comment.setUser(new User());
        comment.setId(1L);
        comment.setComment("New comment");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setRecipeId(2L);
        commentDTO.setUserId(3L);

        when(commentRepository.save(any())).thenReturn(comment);

        CommentDTO commentSaved = commentService.saveOrUpdateComment(commentDTO);

        assertNotNull(commentSaved);
        assertEquals(Long.valueOf(1), commentSaved.getId());
        assertEquals(comment.getComment(), commentSaved.getComment());
        verify(commentRepository, times(1)).save(any());
        verify(commentRepository, never()).saveAll(any());

    }

    @Test
    public void deleteComment() throws Exception {
        Long toDelete = 2L;

        commentService.deleteComment(toDelete);

        verify(commentRepository, times(1)).deleteById(anyLong());
        verify(commentRepository, never()).deleteAll();
    }

}