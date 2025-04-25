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
@Table(name="cornea")
public class Cornea extends IdentifiedModel {
    @Enumerated(EnumType.STRING)
    private Enum.EyeSide eyeSide;
    private boolean normal;
    private boolean corneaGuttata;
    private boolean epithelialDystropia;
    private boolean stromalDystropia;
    private boolean posteriorDystropia;
    private boolean keratoconus;
    private boolean transfixing;
    private boolean dalk;
    private boolean dmek;
    private boolean dsaek;
    private boolean scar;
    private boolean dryEye;
    private boolean fuchsGuttata;
    private boolean fleischerRing;
    private boolean vogtRidge;
    private boolean cornealOpacity;
    private boolean hydrops;
    private boolean dystrophy;
    private boolean endothelial;
    private boolean stromal;
    private boolean epithelial;
    private boolean transplant;
    private boolean pterygium;
    private boolean salzmann;
    private boolean peripheralDegeneration;
    private boolean radialKeratotomy;
    private String radialKeratotomyText;
    private boolean trauma;
    private boolean cornealOpacityHaze;
    private String cornealOpacityHazeLocation;
    private boolean colorantIntake;
    private boolean other;
    private String otherText;
}
