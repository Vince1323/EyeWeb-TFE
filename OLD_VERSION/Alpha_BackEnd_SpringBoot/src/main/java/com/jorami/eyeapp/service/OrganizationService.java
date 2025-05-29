package com.jorami.eyeapp.service;

import com.jorami.eyeapp.model.Organization;
import com.jorami.eyeapp.model.UserOrganizationRole;

import java.util.List;

public interface OrganizationService {

    //GET
    List<Organization> getOrganizationsByUserId(Long userId);


    //POST
    List<UserOrganizationRole> addOrganization(Long userId, List<Long> organizationsId);
}