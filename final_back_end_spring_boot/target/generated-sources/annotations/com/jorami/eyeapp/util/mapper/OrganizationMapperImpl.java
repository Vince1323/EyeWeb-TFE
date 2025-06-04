package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.OrganizationDto;
import com.jorami.eyeapp.model.Organization;
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
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public Organization toOrganization(OrganizationDto organizationDto) {
        if ( organizationDto == null ) {
            return null;
        }

        Organization.OrganizationBuilder organization = Organization.builder();

        organization.name( organizationDto.getName() );
        if ( organizationDto.getIsGlobal() != null ) {
            organization.isGlobal( organizationDto.getIsGlobal() );
        }

        return organization.build();
    }

    @Override
    public OrganizationDto toOrganizationDto(Organization organization) {
        if ( organization == null ) {
            return null;
        }

        OrganizationDto organizationDto = new OrganizationDto();

        organizationDto.setId( organization.getId() );
        organizationDto.setVersion( organization.getVersion() );
        organizationDto.setName( organization.getName() );

        return organizationDto;
    }

    @Override
    public List<Organization> toOrganizations(List<OrganizationDto> organizationDtos) {
        if ( organizationDtos == null ) {
            return null;
        }

        List<Organization> list = new ArrayList<Organization>( organizationDtos.size() );
        for ( OrganizationDto organizationDto : organizationDtos ) {
            list.add( toOrganization( organizationDto ) );
        }

        return list;
    }

    @Override
    public List<OrganizationDto> toOrganizationDtos(List<Organization> organizations) {
        if ( organizations == null ) {
            return null;
        }

        List<OrganizationDto> list = new ArrayList<OrganizationDto>( organizations.size() );
        for ( Organization organization : organizations ) {
            list.add( toOrganizationDto( organization ) );
        }

        return list;
    }
}
