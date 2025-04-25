package com.jorami.eyeapp.dto.patient;

import com.jorami.eyeapp.dto.OrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientSummaryDto {

    private long id;
    private String lastname;
    private String firstname;
    private String birthDate;
    private long version;
    private List<OrganizationDto> organizations;

}

