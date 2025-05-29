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
@Table(name="retina")
public class Retina extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private boolean normal;
    private boolean dmla;
    private String dmlaText;
    private boolean diabeticRetinopathy;
    private boolean epiretinalMembrane;
    private boolean vitreomacularTraction;
    private boolean crscSequel;
    private boolean ovcr;
    private boolean oacr;
    private boolean macularOedema;
    private boolean diabetes;
    private boolean oedemDdmla;
    private boolean avcr;
    private boolean oedemaOther;
    private String oedemaOtherText;
    private boolean other;
    private String otherText;
}
