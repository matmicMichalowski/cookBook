package com.matmic.cookbook.mapper;

import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    UserDTO userToUserDto(User user);

    User userDtoToUser(UserDTO userDTO);
}
