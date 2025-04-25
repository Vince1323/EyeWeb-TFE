package com.jorami.eyeapp.model.consultation;


import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.Link;
import jakarta.persistence.*;

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
@Table(name = "consultation")
public class Consultation extends IdentifiedModel {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consultation")
    @NotAudited
    private List<Link> links;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = AnteriorChamber.class)
    private List<AnteriorChamber> anteriorChambers;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Conjonctive.class)
    private List<Conjonctive> conjonctives;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Cornea.class)
    private List<Cornea> corneas;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Cristallin.class)
    private List<Cristallin> cristallins;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = DominantDirector.class)
    private List<DominantDirector> dominantDirectors;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Eyelid.class)
    private List<Eyelid> eyelids;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = FreeEdge.class)
    private List<FreeEdge> freeEdges;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = ObjectiveRefraction.class)
    private List<ObjectiveRefraction> objectiveRefractions;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Oct.class)
    private List<Oct> octs;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = OpticNerve.class)
    private List <OpticNerve> opticNerves;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Pupillometry.class)
    private List<Pupillometry> pupillometries;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Retina.class)
    private List<Retina> retinas;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = ScopeCorrection.class)
    private List<ScopeCorrection> scopeCorrections;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Specular.class)
    private List<Specular> speculars;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = SubjectiveRefraction.class)
    private List<SubjectiveRefraction> subjectiveRefractions;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = TearFilm.class)
    private List<TearFilm> tearFilms;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = UncorrectedAcuity.class)
    private List<UncorrectedAcuity> uncorrectedAcuities;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = CorrectedAcuity.class)
    private List<CorrectedAcuity> correctedAcuities;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Vitreous.class)
    private List<Vitreous> vitreous;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Remark.class)
    private List<Remark> remarks;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = SideEffect.class)
    private List<SideEffect> sideEffects;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Dli.class)
    private List<Dli> dlis;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Diagnostic.class)
    private List<Diagnostic> diagnostics;
    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Conclusion.class)
    private List<Conclusion> conclusions;
}
