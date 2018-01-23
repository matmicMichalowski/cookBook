package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.CommentToCommentDto;
import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.repository.CommentRepository;
import com.matmic.cookbook.repository.RecipeRepository;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.CommentService;
import com.matmic.cookbook.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CommentControllerTest {

    private final String TEST_COMMENT = "TestComment";

    @Mock
    private CommentService commentService;


    private CommentToCommentDto toCommentDto = new CommentToCommentDto();

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    private MockMvc mockMvc;

    private Comment comment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Authentication auth = new UsernamePasswordAuthenticationToken("UserTest", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        final CommentController controller = new CommentController(commentService, userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
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

        CommentDTO commentDTO = toCommentDto.convert(comment);

        when(commentService.findCommentById(anyLong())).thenReturn(commentDTO);

        mockMvc.perform(get("/api/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.comment").value(TEST_COMMENT));
    }

    @Test
    public void getAllComments() throws Exception {

        List<CommentDTO> comments = new ArrayList<>();
        CommentDTO commentDTO = toCommentDto.convert(comment);
        commentDTO.setUserName("TestUser");
        comments.add(commentDTO);

        Page<CommentDTO> page = new PageImpl<>(comments);

        when(commentService.getAllComments(any())).thenReturn(page);

        mockMvc.perform(get("/api/comments?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(TEST_COMMENT)));

        assertEquals(1, page.getTotalElements());

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


        when(commentService.saveNewComment(any())).thenReturn(Optional.of(commentSaved));

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
        commentDTO.setUserName("UserTest");
        commentDTO.setRecipeId(2L);
        commentDTO.setUserId(3L);

        CommentDTO commentSaved = new CommentDTO();
        commentSaved.setId(1L);
        commentSaved.setComment(commentDTO.getComment());
        commentSaved.setUserName("UserTest");
        commentSaved.setRecipeId(commentDTO.getRecipeId());
        commentSaved.setUserId(commentDTO.getUserId());


        when(commentService.saveOrUpdateComment(any())).thenReturn(commentSaved);

        mockMvc.perform(put("/api/comment/update")
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