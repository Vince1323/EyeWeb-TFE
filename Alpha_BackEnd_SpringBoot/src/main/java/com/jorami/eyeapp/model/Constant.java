package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@SuperBuilder
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="constant")
public class Constant extends IdentifiedModel {

    private String constantType;
    private String value;
    private String formula;

    @ManyToOne
    @JoinColumn(name = "calcul_id")
    @JsonIgnore
    private Calcul calcul;

}
