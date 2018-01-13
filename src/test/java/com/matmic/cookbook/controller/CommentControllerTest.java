package com.matmic.cookbook.controller;

import com.matmic.cookbook.CookbookApplication;
import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import com.matmic.cookbook.service.CommentService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CookbookApplication.class)
public class CommentControllerTest {

    private final String TEST_COMMENT = "TestComment";

    @Mock
    CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private MockMvc mockMvc;

    private Comment comment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

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
    @Transactional
    public void getComment() throws Exception {

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setComment(TEST_COMMENT);

        //commentRepository.saveAndFlush(comment);
        List<CommentDTO> comments = new ArrayList<>();
        comments.add(commentDTO);

        when(commentService.getAllComments()).thenReturn(comments);

        mockMvc.perform(get("/api/comment/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(TEST_COMMENT)));

    }

    @Test
    public void getAllComments() throws Exception {
    }

    @Test
    public void createNewComment() throws Exception {
    }

    @Test
    public void updateComment() throws Exception {
    }

    @Test
    public void deleteComment() throws Exception {
    }

}