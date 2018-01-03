package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CommentDtoToCommentTest {
    public static final Long ID_VALUE = 2L;
    public static final String COMMENT = "New comment body";
    public static final Long RECIPE_ID = 1L;
    public static final Long USER_ID = 4L;

    private CommentDtoToComment converter;

    @Before
    public void setUp() throws Exception {
        converter = new CommentDtoToComment();
    }

    @Test
    public void convert() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(ID_VALUE);
        commentDTO.setComment(COMMENT);
        commentDTO.setUserId(USER_ID);
        commentDTO.setRecipeId(RECIPE_ID);

        Comment comment = converter.convert(commentDTO);

        assertNotNull(comment);
        assertEquals(Long.valueOf(4L), comment.getUser().getId());
        assertEquals(Long.valueOf(1L), comment.getRecipe().getId());
    }

}