package com.jorami.eyeapp.model.operation;

import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.Workflow;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "workflow_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowSummary extends IdentifiedModel {

    @OneToOne
    @JoinColumn(name = "workflow_id", referencedColumnName = "id")
    private Workflow workflow;

    private String atcdO;
    private String medicalHistory;
    private String dominantEye;
    private String allergy;
    private String anesthesia;
    private String surgeryType;

    private LocalDate dateSurgery;
    private String laterality;
    private String surgeon;
    private String visitWithSurgeon;

    private String iol;

    @ElementCollection
    private List<String> operatingMaterials;

    @ElementCollection
    private List<String> preopTreatments;

    @ElementCollection
    private List<String> postopAppointments;

}
