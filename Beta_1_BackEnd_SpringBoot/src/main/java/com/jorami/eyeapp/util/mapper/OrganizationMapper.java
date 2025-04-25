package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.OrganizationDto;
import com.jorami.eyeapp.model.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserOrganizationRoleMapper.class})
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    Organization toOrganization(OrganizationDto organizationDto);
    OrganizationDto toOrganizationDto(Organization organization);

    List<Organization> toOrganizations(List<OrganizationDto> organizationDtos);
    List<OrganizationDto> toOrganizationDtos(List<Organization> organizations);
}