package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.UserOrganizationRoleDto;
import com.jorami.eyeapp.model.UserOrganizationRole;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserOrganizationRoleMapperImpl implements UserOrganizationRoleMapper {

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserOrganizationRole toUserOrganizationRole(UserOrganizationRoleDto userOrganizationRoleDto) {
        if ( userOrganizationRoleDto == null ) {
            return null;
        }

        UserOrganizationRole.UserOrganizationRoleBuilder userOrganizationRole = UserOrganizationRole.builder();

        userOrganizationRole.user( userMapper.toUser( userOrganizationRoleDto.getUser() ) );
        userOrganizationRole.isAdmin( userOrganizationRoleDto.getIsAdmin() );
        userOrganizationRole.organization( organizationMapper.toOrganization( userOrganizationRoleDto.getOrganization() ) );

        return userOrganizationRole.build();
    }

    @Override
    public UserOrganizationRoleDto toUserOrganizationRoleDto(UserOrganizationRole userOrganizationRole) {
        if ( userOrganizationRole == null ) {
            return null;
        }

        UserOrganizationRoleDto userOrganizationRoleDto = new UserOrganizationRoleDto();

        userOrganizationRoleDto.setUser( userMapper.toUserDto( userOrganizationRole.getUser() ) );
        userOrganizationRoleDto.setIsAdmin( userOrganizationRole.getIsAdmin() );
        userOrganizationRoleDto.setOrganization( organizationMapper.toOrganizationDto( userOrganizationRole.getOrganization() ) );

        return userOrganizationRoleDto;
    }

    @Override
    public List<UserOrganizationRole> toUserOrganizationRoles(List<UserOrganizationRoleDto> userOrganizationRoleDtos) {
        if ( userOrganizationRoleDtos == null ) {
            return null;
        }

        List<UserOrganizationRole> list = new ArrayList<UserOrganizationRole>( userOrganizationRoleDtos.size() );
        for ( UserOrganizationRoleDto userOrganizationRoleDto : userOrganizationRoleDtos ) {
            list.add( toUserOrganizationRole( userOrganizationRoleDto ) );
        }

        return list;
    }

    @Override
    public List<UserOrganizationRoleDto> toUserOrganizationRoleDtos(List<UserOrganizationRole> userOrganizationRoles) {
        if ( userOrganizationRoles == null ) {
            return null;
        }

        List<UserOrganizationRoleDto> list = new ArrayList<UserOrganizationRoleDto>( userOrganizationRoles.size() );
        for ( UserOrganizationRole userOrganizationRole : userOrganizationRoles ) {
            list.add( toUserOrganizationRoleDto( userOrganizationRole ) );
        }

        return list;
    }
}
