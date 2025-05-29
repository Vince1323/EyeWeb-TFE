package com.jorami.eyeapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="organization")
public class Organization extends IdentifiedModel {

    private String name;
    private boolean isGlobal;

    @OneToMany(mappedBy = "organization")
    @NotAudited
    private List<UserOrganizationRole> userOrganizationRoles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @NotAudited
    private List<Link> links;

    public Organization(Long id, String name) {
        setId(id);
        setName(name);
    }
}

