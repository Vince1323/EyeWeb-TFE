package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="lens_manufacturer")
public class LensManufacturer extends IdentifiedModel {

    private String name;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Lens.class, mappedBy = "lensManufacturer")
    @Fetch(FetchMode.SELECT)
    private List<Lens> lenses;

}