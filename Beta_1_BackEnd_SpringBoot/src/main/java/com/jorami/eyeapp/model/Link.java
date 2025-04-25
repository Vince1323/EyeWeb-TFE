package com.jorami.eyeapp.model;


import com.jorami.eyeapp.model.consultation.Consultation;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.operation.Planning;
import com.jorami.eyeapp.model.operation.Protocol;
import com.jorami.eyeapp.model.patient.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "link")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrganization")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPatient")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idConsultation")
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idExam")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlanning")
    private Planning planning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProtocol")
    private Protocol protocol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idWorkflow")
    private Workflow workflow;

}
