package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.domain.Recipe;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.CommentDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommentToCommentDtoTest {
    public static final Long ID_VALUE = 2L;
    public static final String COMMENT = "New comment body";
    public static final Long RECIPE_ID = 1L;
    public static final Long USER_ID = 4L;


    private CommentToCommentDto converter;

    @Before
    public void setUp() throws Exception {
        converter = new CommentToCommentDto();
    }

    @Test
    public void convert() throws Exception {
        Comment comment = new Comment();
        comment.setId(ID_VALUE);
        comment.setComment(COMMENT);
        comment.setUser(new User());
        comment.getUser().setId(USER_ID);
        comment.setRecipe(new Recipe());
        comment.getRecipe().setId(RECIPE_ID);

        CommentDTO commentDTO = converter.convert(comment);

        assertNotNull(commentDTO);
        assertEquals(comment.getUser().getId(), commentDTO.getUserId());
        assertEquals(comment.getRecipe().getId(), commentDTO.getRecipeId());
        assertEquals(comment.getComment(), commentDTO.getComment());
    }

}