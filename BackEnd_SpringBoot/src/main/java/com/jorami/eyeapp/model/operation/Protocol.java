package com.jorami.eyeapp.model.operation;

import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.Link;
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
@Table(name = "protocol")
public class Protocol extends IdentifiedModel {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "protocol")
    @NotAudited
    private List<Link> links;

}