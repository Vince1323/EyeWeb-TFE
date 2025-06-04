package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.UserDto;
import com.jorami.eyeapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, AddressMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "address.boxNumber", target = "address.boxNumber")
    User toUser(UserDto userDto);

    @Mapping(source = "address.boxNumber", target = "address.boxNumber")
    UserDto toUserDto(User user);

    List<User> toUsers(List<UserDto> userDtos);
    List<UserDto> toUserDtos(List<User> users);
}
