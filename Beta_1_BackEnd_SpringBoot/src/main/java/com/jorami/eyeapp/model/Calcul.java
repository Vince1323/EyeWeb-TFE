package com.jorami.eyeapp.model;

import com.jorami.eyeapp.model.exam.Exam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
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
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="calcul")
public class Calcul extends IdentifiedModel {

    @Enumerated(EnumType.STRING)
    private com.jorami.eyeapp.model.Enum.EyeSide eyeSide;
    private Float targetRefraction;
    @ColumnDefault("false")
    private Boolean isSecondEye;
    private Float precPowerSelected;
    private Float se;
    private Long idReferencePrecExam;
    private Long idReferencePrecCalcul;

    @ManyToOne
    @JoinColumn(name="exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "lens_id")
    private Lens lens;

    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY, targetEntity = Constant.class, mappedBy = "calcul")
    @Fetch(FetchMode.SELECT)
    private List<Constant> constants;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Diopter.class, mappedBy = "calcul")
    @Fetch(FetchMode.SELECT)
    @NotAudited
    private List<Diopter> diopters;

}
