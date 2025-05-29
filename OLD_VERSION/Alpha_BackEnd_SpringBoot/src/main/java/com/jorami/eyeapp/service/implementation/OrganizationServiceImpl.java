package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.Organization;
import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.model.UserOrganizationRole;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.repository.UserOrganizationRoleRepository;
import com.jorami.eyeapp.repository.UserRepository;
import com.jorami.eyeapp.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;


    @Override
    public List<Organization> getOrganizationsByUserId(Long userId) {
        return organizationRepository.findOrganizationsByUserId(userId);
    }

    // TODO: erroné pour certains cas (par khaled)
    @Override
    public List<UserOrganizationRole> addOrganization(Long userId, List<Long> organizationsId) {
        User user = userRepository.findUserById(userId);
        List<Organization> organizations = organizationRepository.findOrganizationsById(organizationsId);
        List<UserOrganizationRole> userOrganizations = userOrganizationRoleRepository.findUserOrganizationsById(organizationsId);

        if(userOrganizations.isEmpty()) {
            for(final Organization organization: organizations) {
                userOrganizations.add(UserOrganizationRole.builder().user(user).isAdmin(false).organization(organization).build());
            }
        } else {
            userOrganizations = userOrganizations.stream()
                    .filter(userOrganizationRole ->
                            user.getUserOrganizationRoles().stream()
                                    .noneMatch(userRole -> Objects.equals(userRole.getOrganization().getId(), userOrganizationRole.getOrganization().getId())))
                    .toList();
        }
        for(final UserOrganizationRole userOrganization : userOrganizations) {
            userOrganizationRoleRepository.save(UserOrganizationRole.builder().user(user).isAdmin(false).organization(userOrganization.getOrganization()).build());
        }

        return userOrganizations;
    }

}
