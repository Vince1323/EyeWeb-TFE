package com.jorami.eyeapp.model.operation;

import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.Link;
import com.jorami.eyeapp.model.exam.Exam;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name = "planning")

public class Planning extends IdentifiedModel {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "planning")
    @NotAudited
    private List<Link> links;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(name = "eye_side")
    private String eyeSide;

    @Column(name = "planning_date")
    private LocalDate planningDate;

    @Column(name = "location")
    private String location;

    @Column(name = "surgeon")
    private String surgeon;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}

