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
@Table(name="optic_nerve")
public class OpticNerve extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private boolean normal;
    private boolean anteriorIschemic;
    private boolean posterior;
    private boolean other;
    private String otherText;
    private boolean glaucoma;
    private String glaucomaExcavation;
    private boolean bleeding;
    private boolean neuropatia;
}
