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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@SuperBuilder
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="diopter")
public class Diopter extends IdentifiedModel {

    private Float iolPower;
    private Float value;
    private String formula;
    @ColumnDefault("false")
    private boolean selected;

    @ManyToOne
    @JoinColumn(name="calcul_id")
    @NotAudited
    @JsonIgnore
    private Calcul calcul;

}
