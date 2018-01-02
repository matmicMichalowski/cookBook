package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.Comment;
import com.matmic.cookbook.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

   CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

   @Mapping(source = "id", target = "id")
   CommentDTO commentToCommentDto(Comment comment);

   @Mapping(source = "id", target = "id")
   Comment commentDtoToComment(CommentDTO commentDTO);
}
