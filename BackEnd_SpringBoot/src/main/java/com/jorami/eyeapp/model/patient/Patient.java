package com.jorami.eyeapp.model.patient;

import com.jorami.eyeapp.model.*;
import com.jorami.eyeapp.model.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="patient")
public class Patient extends IdentifiedModel {

    private String lastname;
    private String firstname;
    private LocalDate birthDate;
    private String niss;
    @Enumerated(EnumType.STRING)
    private com.jorami.eyeapp.model.Enum.Gender gender;
    private String phone;
    private String mail;
    private String job;
    private String hobbies;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "patient")
    @NotAudited
    private List<Link> links;

    @Transient
    private List<Organization> organizations = new ArrayList<>();


    /**
     * Constructeur utilisé pour retourner le récapitulatif d'un patient (PatientSummary).
     * @param lastname Le nom du patient.
     * @param firstname Le prénom du patient.
     * @param birthDate La date d'anniversaire du patient.
     * @param niss Le niss du patient.
     * @param gender Le genre du patient.
     * @param phone Le téléphone du patient.
     * @param mail L'adresse mail du patient.
     * @param job Le métier du patient.
     * @param hobbies Les loisirs du patient.
     */
    public Patient(Long id, String lastname, String firstname, LocalDate birthDate, String niss, Enum.Gender gender, String phone, String mail, String job, String hobbies) {
        this.setId(id);
        this.setLastname(lastname);
        this.setFirstname(firstname);
        this.setBirthDate(birthDate);
        this.setNiss(niss);
        this.setGender(gender);
        this.setPhone(phone);
        this.setMail(mail);
        this.setJob(job);
        this.setHobbies(hobbies);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birthDate=" + birthDate +
                ", niss='" + niss + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", job='" + job + '\'' +
                ", hobbies='" + hobbies + '\'' +
                ", deleted=" + getDeleted() +
                '}';
    }
    // Vérification automatique pour éviter NULL
    @Override
    public Boolean getDeleted() {
        return super.getDeleted() != null ? super.getDeleted() : false;
    }
    public String getFullName() {
        return getLastname() + " " + getFirstname();
    }


    public void addLink(Link link) {
        links.add(link);
        link.setPatient(this);
    }

}
