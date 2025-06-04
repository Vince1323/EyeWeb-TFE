package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.AddressDto;
import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.dto.patient.PatientSummaryDto;
import com.jorami.eyeapp.model.Address;
import com.jorami.eyeapp.model.patient.Patient;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Patient toPatient(PatientDto patientDto) {
        if ( patientDto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setAddress( addressDtoToAddress( patientDto.getAddress() ) );
        patient.setBirthDate( PatientMapper.stringToLocalDate( patientDto.getBirthDate() ) );
        patient.setGender( PatientMapper.stringToGender( patientDto.getGender() ) );
        patient.setId( patientDto.getId() );
        patient.setVersion( patientDto.getVersion() );
        patient.setLastname( patientDto.getLastname() );
        patient.setFirstname( patientDto.getFirstname() );
        patient.setNiss( patientDto.getNiss() );
        patient.setPhone( patientDto.getPhone() );
        patient.setMail( patientDto.getMail() );
        patient.setJob( patientDto.getJob() );
        patient.setHobbies( patientDto.getHobbies() );

        return patient;
    }

    @Override
    public PatientDto toPatientDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDto patientDto = new PatientDto();

        patientDto.setAddress( addressToAddressDto( patient.getAddress() ) );
        patientDto.setBirthDate( PatientMapper.localDateToString( patient.getBirthDate() ) );
        patientDto.setGender( PatientMapper.genderToString( patient.getGender() ) );
        if ( patient.getId() != null ) {
            patientDto.setId( patient.getId() );
        }
        if ( patient.getVersion() != null ) {
            patientDto.setVersion( patient.getVersion() );
        }
        patientDto.setLastname( patient.getLastname() );
        patientDto.setFirstname( patient.getFirstname() );
        patientDto.setNiss( patient.getNiss() );
        patientDto.setPhone( patient.getPhone() );
        patientDto.setMail( patient.getMail() );
        patientDto.setJob( patient.getJob() );
        patientDto.setHobbies( patient.getHobbies() );

        return patientDto;
    }

    @Override
    public PatientSummaryDto toPatientSummaryDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientSummaryDto patientSummaryDto = new PatientSummaryDto();

        patientSummaryDto.setBirthDate( PatientMapper.localDateToString( patient.getBirthDate() ) );
        if ( patient.getId() != null ) {
            patientSummaryDto.setId( patient.getId() );
        }
        patientSummaryDto.setLastname( patient.getLastname() );
        patientSummaryDto.setFirstname( patient.getFirstname() );
        if ( patient.getVersion() != null ) {
            patientSummaryDto.setVersion( patient.getVersion() );
        }
        patientSummaryDto.setOrganizations( organizationMapper.toOrganizationDtos( patient.getOrganizations() ) );

        return patientSummaryDto;
    }

    @Override
    public List<Patient> toPatients(List<PatientDto> patientDtos) {
        if ( patientDtos == null ) {
            return null;
        }

        List<Patient> list = new ArrayList<Patient>( patientDtos.size() );
        for ( PatientDto patientDto : patientDtos ) {
            list.add( toPatient( patientDto ) );
        }

        return list;
    }

    @Override
    public List<PatientDto> toPatientDtos(List<Patient> patients) {
        if ( patients == null ) {
            return null;
        }

        List<PatientDto> list = new ArrayList<PatientDto>( patients.size() );
        for ( Patient patient : patients ) {
            list.add( toPatientDto( patient ) );
        }

        return list;
    }

    @Override
    public List<PatientSummaryDto> toPatientSummaryDtos(List<Patient> patients) {
        if ( patients == null ) {
            return null;
        }

        List<PatientSummaryDto> list = new ArrayList<PatientSummaryDto>( patients.size() );
        for ( Patient patient : patients ) {
            list.add( toPatientSummaryDto( patient ) );
        }

        return list;
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setBoxNumber( addressDto.getBoxNumber() );
        address.setId( addressDto.getId() );
        address.setVersion( addressDto.getVersion() );
        address.setStreet( addressDto.getStreet() );
        address.setStreetNumber( addressDto.getStreetNumber() );
        address.setZipCode( addressDto.getZipCode() );
        address.setCity( addressDto.getCity() );
        address.setCountry( addressDto.getCountry() );

        return address;
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setBoxNumber( address.getBoxNumber() );
        addressDto.setId( address.getId() );
        addressDto.setVersion( address.getVersion() );
        addressDto.setStreet( address.getStreet() );
        addressDto.setStreetNumber( address.getStreetNumber() );
        addressDto.setZipCode( address.getZipCode() );
        addressDto.setCity( address.getCity() );
        addressDto.setCountry( address.getCountry() );

        return addressDto;
    }
}
