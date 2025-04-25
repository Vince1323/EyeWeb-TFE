package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.RoleDto;
import com.jorami.eyeapp.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role toRole(RoleDto roleDto);
    RoleDto toRoleDto(Role role);

    List<Role> toRoles(List<RoleDto> roleDtos);
    List<RoleDto> toRoleDtos(List<Role> roles);
}
