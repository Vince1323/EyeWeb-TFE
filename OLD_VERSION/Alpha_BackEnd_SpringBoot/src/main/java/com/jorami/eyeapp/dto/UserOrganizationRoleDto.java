package com.jorami.eyeapp.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOrganizationRoleDto {

    private UserDto user;
    private Boolean isAdmin;
    private OrganizationDto organization;

}