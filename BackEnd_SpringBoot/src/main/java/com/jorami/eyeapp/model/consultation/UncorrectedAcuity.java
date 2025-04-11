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
@Table(name="uncorrected_acuity")
public class UncorrectedAcuity extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private Float distanceVisualAcuity;
    private Float distanceVisualAcuityOU;
    private Float intermediateVisualAcuity;
    private Float intermediateVisualAcuityOU;
    private Float nearVisualAcuity;
    private Float nearVisualAcuityOU;

}
