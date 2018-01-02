package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    UserDTO userToUserDto(User user);

    @Mapping(source = "id", target = "id")
    User userDtoToUser(UserDTO userDTO);
}
