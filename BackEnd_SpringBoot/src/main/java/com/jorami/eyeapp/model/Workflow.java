package com.jorami.eyeapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name = "workflow")
public class Workflow extends IdentifiedModel {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflow")
    @NotAudited
    private List<Link> links;

}
