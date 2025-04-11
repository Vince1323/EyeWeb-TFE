package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.dto.patient.PatientSummaryDto;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.util.DateFunction;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;


@Mapper(componentModel = "spring", uses = {AddressMapper.class, OrganizationMapper.class})
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "stringToLocalDate")
    @Mapping(source = "gender", target = "gender", qualifiedByName = "stringToGender")
    @Mapping(source = "address.boxNumber", target = "address.boxNumber")
    Patient toPatient(PatientDto patientDto);

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    @Mapping(source = "gender", target = "gender", qualifiedByName = "genderToString")
    @Mapping(source = "address.boxNumber", target = "address.boxNumber")
    PatientDto toPatientDto(Patient patient);

    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "localDateToString")
    PatientSummaryDto toPatientSummaryDto(Patient patient);

    List<Patient> toPatients(List<PatientDto> patientDtos);
    List<PatientDto> toPatientDtos(List<Patient> patients);
    List<PatientSummaryDto> toPatientSummaryDtos(List<Patient> patients);

    @Named("stringToLocalDate")
    static LocalDate stringToLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return DateFunction.parseDateLocalDate(date);
    }

    @Named("localDateToString")
    static String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DateFunction.findFormatLocalDateAndParseString(date);
    }

    @Named("stringToGender")
    static Enum.Gender stringToGender(String gender) {
        if (gender == null) {
            return null;
        }
        try {
            Enum.Gender convertedGender = Enum.Gender.valueOf(gender.toUpperCase());
            return convertedGender;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    @Named("genderToString")
    static String genderToString(Enum.Gender gender) {
        String result = gender != null ? gender.name() : null;
        return result;
    }
}

