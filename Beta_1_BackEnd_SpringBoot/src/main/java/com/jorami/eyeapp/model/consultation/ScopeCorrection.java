package com.jorami.eyeapp.model.consultation;

import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.IdentifiedModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="scope_correction")
public class ScopeCorrection extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private Float glassesSphere;
    private Float glassesCylinder;
    private Float glassesAxis;
    private Float glassesVisualAcuity;
    private Float lensesSphere;
    private Float lensesCylinder;
    private Float lensesAxis;
    private Float lensesVisualAcuity;
    private Float glassesEqSphere;
    private Float glassesEqCylinder;
    private Float glassesEqAxis;
    private Float glassesEqVisualAcuity;
}