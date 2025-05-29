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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientDto {

    private long id;
    private long version;

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(max = 50, message = ConstantMessage.SIZE_VALIDATION + "50.")
    private String lastname;
    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(max = 50, message = ConstantMessage.SIZE_VALIDATION + "50.")
    private String firstname;
    @NotNull(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String birthDate;
    private String niss;
    private String gender;
    private String phone;
    @Email(message = ConstantMessage.VALIDATION_EMAIL)
    private String mail;
    private String job;
    private String hobbies;
    private AddressDto address;
    @JsonIgnore
    private List<OrganizationDto> organizationsDto;
    private List<ExamDto> examDtos;


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
