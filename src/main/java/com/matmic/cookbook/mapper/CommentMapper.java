package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

   CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

   CommentDTO commentToCommentDto(Comment comment);

   Comment commentDtoToComment(CommentDTO commentDTO);
}
