package com.apapedia.user.DTO;

import com.apapedia.user.DTO.request.CreateUserDTO;
import com.apapedia.user.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users userDTOToUser(CreateUserDTO userDTO);
}
