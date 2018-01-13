package com.matmic.cookbook.controller;

import com.matmic.cookbook.CookbookApplication;
import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.CommentDtoToComment;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import com.matmic.cookbook.service.CommentService;
import com.matmic.cookbook.service.CommentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CookbookApplication.class)
public class CommentControllerTest {

    private final String TEST_COMMENT = "TestComment";

    @Mock
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private MockMvc mockMvc;

    private Comment comment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        commentService = new CommentServiceImpl(commentRepository, new CommentToCommentDto(), new CommentDtoToComment());

        final CommentController controller = new CommentController(commentService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        comment = new Comment();
        comment.setId(1L);
        comment.setUser(new User());
        comment.getUser().setId(2L);
        comment.getUser().getComments().add(comment);
        comment.setRecipe(new Recipe());
        comment.getRecipe().setId(3L);
        comment.getRecipe().getComments().add(comment);
        comment.setComment(TEST_COMMENT);
    }


    @Test
    public void getComment() throws Exception {

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/api/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.comment").value(TEST_COMMENT));
    }

    @Test
    public void getAllComments() throws Exception {

        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findAll()).thenReturn(comments);

        mockMvc.perform(get("/api/comment/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(TEST_COMMENT)));
    }

    @Test
    public void createNewComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment(TEST_COMMENT);
        commentDTO.setRecipeId(2L);
        commentDTO.setUserId(3L);

        CommentDTO commentSaved = new CommentDTO();
        commentSaved.setId(1L);
        commentSaved.setComment(commentDTO.getComment());
        commentSaved.setRecipeId(commentDTO.getRecipeId());
        commentSaved.setUserId(commentDTO.getUserId());


        when(commentRepository.save(any())).thenReturn(comment);
        when(commentService.saveOrUpdateComment(commentDTO)).thenReturn(commentSaved);

        mockMvc.perform(post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonBytes(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value(TEST_COMMENT));
    }

    @Test
    public void updateComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setComment(TEST_COMMENT);
        commentDTO.setRecipeId(2L);
        commentDTO.setUserId(3L);


        CommentDTO commentSaved = new CommentDTO();
        commentSaved.setId(1L);
        commentSaved.setComment(commentDTO.getComment());
        commentSaved.setRecipeId(commentDTO.getRecipeId());
        commentSaved.setUserId(commentDTO.getUserId());


        when(commentRepository.save(any())).thenReturn(comment);
        when(commentService.saveOrUpdateComment(commentDTO)).thenReturn(commentSaved);

        mockMvc.perform(put("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonBytes(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(commentSaved.getComment()));

    }


    @Test
    public void deleteComment() throws Exception {

        mockMvc.perform(delete("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());

    }

}