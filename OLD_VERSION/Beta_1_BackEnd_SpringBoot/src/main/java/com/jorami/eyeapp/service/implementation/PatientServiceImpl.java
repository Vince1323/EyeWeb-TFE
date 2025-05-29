package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.Address;
import com.jorami.eyeapp.model.Link;
import com.jorami.eyeapp.model.Organization;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.repository.AddressRepository;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.repository.PatientRepository;
import com.jorami.eyeapp.service.AddressService;
import com.jorami.eyeapp.service.PatientService;
import com.jorami.eyeapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final AddressRepository addressRepository;

    @Override
    public List<Patient> getAllPatientsSummariesByOrganizationsId(List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        List<Patient> patients = patientRepository.findAllPatientsSummariesByOrganizationsId(organizationsId);
        if (patients == null || patients.isEmpty()) {
            throw new NoSuchElementException(LIST_ITEM_NOT_FOUND);
        }
        patients.forEach(patient ->
                patient.setOrganizations(organizationRepository.findOrganizationsByPatientId(patient.getId()))
        );
        return patients;
    }

    @Override
    public Patient getPatientById(List<Long> organizationsId, Long patientId) {
        userService.validOrganizations(organizationsId);
        return patientRepository.findPatientById(patientId, organizationsId).orElse(null);
    }

    @Transactional
    @Override
    public Patient editPatient(Long patientId, Patient updatedPatient, List<Long> organizationsId) {
        return patientRepository.findById(patientId).map(existingPatient -> {

            // Mise à jour des infos générales
            existingPatient.setFirstname(updatedPatient.getFirstname());
            existingPatient.setLastname(updatedPatient.getLastname());
            existingPatient.setBirthDate(updatedPatient.getBirthDate());
            existingPatient.setNiss(updatedPatient.getNiss());
            existingPatient.setGender(updatedPatient.getGender());
            existingPatient.setPhone(updatedPatient.getPhone());
            existingPatient.setMail(updatedPatient.getMail());
            existingPatient.setJob(updatedPatient.getJob());
            existingPatient.setHobbies(updatedPatient.getHobbies());

            // Vérifie si une adresse doit être créée ou mise à jour
            if (updatedPatient.getAddress() != null) {
                if (existingPatient.getAddress() != null) {

                    Address existingAddress = existingPatient.getAddress();
                    existingAddress.setStreet(updatedPatient.getAddress().getStreet());
                    existingAddress.setStreetNumber(updatedPatient.getAddress().getStreetNumber());
                    existingAddress.setBoxNumber(updatedPatient.getAddress().getBoxNumber());
                    existingAddress.setZipCode(updatedPatient.getAddress().getZipCode());
                    existingAddress.setCity(updatedPatient.getAddress().getCity());
                    existingAddress.setCountry(updatedPatient.getAddress().getCountry());
                    existingAddress.setModifiedBy("User");
                    existingAddress.setModifiedAt(LocalDateTime.now());


                    // Sauvegarde explicite de l'adresse mise à jour
                    addressRepository.save(existingAddress);

                } else {
                    // Adresse NULL → Création d'une nouvelle adresse

                    Address newAddress = new Address();
                    newAddress.setStreet(updatedPatient.getAddress().getStreet());
                    newAddress.setStreetNumber(updatedPatient.getAddress().getStreetNumber());
                    newAddress.setBoxNumber(updatedPatient.getAddress().getBoxNumber());
                    newAddress.setZipCode(updatedPatient.getAddress().getZipCode());
                    newAddress.setCity(updatedPatient.getAddress().getCity());
                    newAddress.setCountry(updatedPatient.getAddress().getCountry());
                    newAddress.setCreatedBy("System");
                    newAddress.setCreatedAt(LocalDateTime.now());
                    newAddress.setModifiedBy("System");
                    newAddress.setModifiedAt(LocalDateTime.now());
                    newAddress.setDeleted(false);
                    newAddress.setVersion(1L);
                    // Supprimer l'ID s'il est défini pour éviter la duplication
                    newAddress.setId(null);
                    // Sauvegarde de la nouvelle adresse
                    Address savedAddress = addressService.createAddress(newAddress);
                    existingPatient.setAddress(savedAddress);
                }
            }

            // Enregistrement du patient avec l'adresse mise à jour
            Patient updatedPatientSaved = patientRepository.save(existingPatient);
            return updatedPatientSaved;
        }).orElseThrow(null);
    }

    @Override
    public Patient deletePatient(Long patientId, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);

        Patient patient = patientRepository.findPatientById(patientId, organizationsId)
                .orElseThrow(null);
        patient.setDeleted(true);
        return patientRepository.saveAndFlush(patient);
    }

    @Override
    public Patient addPatient(Patient patient, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        try {
            List<Patient> patientTmp = patientRepository.findPatientByFirstNameAndLastNameAndBirthDate(
                    patient.getFirstname(), patient.getLastname(), patient.getBirthDate());
            if (patientTmp.isEmpty()) {
                patient = patientRepository.save(patient);
                patient.setLinks(new ArrayList<>());
            } else {
                patient = patientTmp.get(0);
            }

            List<Organization> organizations = patient.getLinks().stream().map(Link::getOrganization).toList();
            for (Long orgId : organizationsId) {
                Organization organization = organizationRepository.findById(orgId)
                        .orElseThrow(() -> new RuntimeException(ORGANIZATION_NOT_FOUND));
                if (!organizations.contains(organization)) {
                    Link link = new Link();
                    link.setPatient(patient);
                    link.setOrganization(organization);
                    patient.addLink(link);
                }
            }
            return patientRepository.save(patient);
        } catch (RuntimeException e) {
            throw new RuntimeException(ADD_USER_ERROR);
        }
    }
}
