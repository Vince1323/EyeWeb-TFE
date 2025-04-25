package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.UserOrganizationRoleDto;
import com.jorami.eyeapp.model.UserOrganizationRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, UserMapper.class})
public interface UserOrganizationRoleMapper {

    UserOrganizationRoleMapper INSTANCE = Mappers.getMapper(UserOrganizationRoleMapper.class);

    UserOrganizationRole toUserOrganizationRole(UserOrganizationRoleDto userOrganizationRoleDto);
    UserOrganizationRoleDto toUserOrganizationRoleDto(UserOrganizationRole userOrganizationRole);

    List<UserOrganizationRole> toUserOrganizationRoles(List<UserOrganizationRoleDto> userOrganizationRoleDtos);
    List<UserOrganizationRoleDto> toUserOrganizationRoleDtos(List<UserOrganizationRole> userOrganizationRoles);
}