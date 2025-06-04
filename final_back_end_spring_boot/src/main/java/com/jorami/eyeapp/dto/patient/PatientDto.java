package com.jorami.eyeapp.dto.patient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jorami.eyeapp.dto.AddressDto;
import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.dto.OrganizationDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.jorami.eyeapp.exception.ConstantMessage;

import java.util.List;

/**
 * PatientDto est un DTO (Data Transfer Object) utilisé pour transférer
 * les informations d'un patient entre le backend et le frontend.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDto {

    /** Identifiant unique du patient */
    private long id;

    /** Version de l'entité (optimistic locking pour la gestion concurrente) */
    private long version;

    /** Nom du patient - champ obligatoire, maximum 50 caractères */
    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(max = 50, message = ConstantMessage.SIZE_VALIDATION + "50.")
    private String lastname;

    /** Prénom du patient - champ obligatoire, maximum 50 caractères */
    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(max = 50, message = ConstantMessage.SIZE_VALIDATION + "50.")
    private String firstname;

    /** Date de naissance du patient - champ obligatoire */
    @NotNull(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String birthDate;

    /** Numéro NISS (numéro national) du patient */
    private String niss;

    /** Sexe du patient (M/F/Autre) */
    private String gender;

    /** Numéro de téléphone du patient */
    private String phone;

    /** Adresse email du patient - validée par @Email */
    @Email(message = ConstantMessage.VALIDATION_EMAIL)
    private String mail;

    /** Profession du patient */
    private String job;

    /** Hobbies ou centres d’intérêt du patient */
    private String hobbies;

    /** Objet représentant l'adresse du patient */
    private AddressDto address;

    /** Liste des organisations (cliniques, hôpitaux) associées - ignorée à la sérialisation JSON */
    @JsonIgnore
    private List<OrganizationDto> organizationsDto;

    /** Liste des examens liés à ce patient */
    private List<ExamDto> examDtos;

    /**
     * Redéfinition du toString() pour faciliter le debug.
     * @return chaîne de caractères représentant un patient
     */
    @Override
    public String toString() {
        return "PatientDto{" +
                "lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birthDate=" + birthDate +
                ", niss='" + niss + '\'' +
                ", gender='" + (gender != null ? gender : "NULL") + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", job='" + job + '\'' +
                ", hobbies='" + hobbies +
                '}';
    }

}
