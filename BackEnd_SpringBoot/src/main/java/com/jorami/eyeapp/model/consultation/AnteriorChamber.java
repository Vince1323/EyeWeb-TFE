package com.jorami.eyeapp.model.consultation;

import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.IdentifiedModel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="anterior_chamber")
public class AnteriorChamber extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private boolean normal;
    private boolean pseudoexfoliation;
    private boolean pigmentDispersion;
    private boolean iridotomy;
    private String iridotomyLocation;
    private boolean lower;
    private boolean uveitis;
    private boolean coloboma;
    private boolean other;
    private String otherText;
}
