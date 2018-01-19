package com.matmic.cookbook.converter;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CommentToCommentDto implements Converter<Comment, CommentDTO>{


    @Synchronized
    @Nullable
    @Override
    public CommentDTO convert(Comment comment) {
        if (comment == null) {
            return null;
        }

        final CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setComment(comment.getComment());
        commentDTO.setRecipeId(comment.getRecipe().getId());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setUserName(comment.getUserName());

        return commentDTO;
    }
}
