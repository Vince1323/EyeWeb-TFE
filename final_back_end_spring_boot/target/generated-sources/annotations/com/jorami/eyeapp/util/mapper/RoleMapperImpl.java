package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.RoleDto;
import com.jorami.eyeapp.model.Role;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role toRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.name( roleDto.getName() );

        return role.build();
    }

    @Override
    public RoleDto toRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setName( role.getName() );

        return roleDto;
    }

    @Override
    public List<Role> toRoles(List<RoleDto> roleDtos) {
        if ( roleDtos == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( roleDtos.size() );
        for ( RoleDto roleDto : roleDtos ) {
            list.add( toRole( roleDto ) );
        }

        return list;
    }

    @Override
    public List<RoleDto> toRoleDtos(List<Role> roles) {
        if ( roles == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>( roles.size() );
        for ( Role role : roles ) {
            list.add( toRoleDto( role ) );
        }

        return list;
    }
}
