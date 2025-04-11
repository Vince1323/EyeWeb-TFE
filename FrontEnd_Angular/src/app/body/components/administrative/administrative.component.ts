    import { Component, OnInit } from '@angular/core';
    import { MessageService } from 'primeng/api';
    import { ConfirmationService } from 'primeng/api';
    import { SelectionService } from '../../service/SelectionService';
    import { Patient } from '../../api/patient/Patient';
    import { PatientService } from '../../service/PatientService';
    import { Router } from '@angular/router';
    import { StorageService } from "../../service/StorageService";
    import { FormBuilder, FormGroup, Validators } from '@angular/forms';

    @Component({
        selector: 'app-administrative',
        templateUrl:'./administrative.component.html',
        styleUrls: ['./administrative.component.scss'],
        providers: [MessageService, ConfirmationService]
    })
    export class AdministrativeComponent implements OnInit {
        messages: any[] = [];
        patient: Patient | null = null;
        isEditMode: boolean = false;
        originalPatient: Patient | null = null; 
        idOrga: number[] = [];
        birthDateValue: Date | null = null;
        patientForm: FormGroup;

        isValidFirstname: boolean = true;
        isValidLastname: boolean = true;
        isValidSSN: boolean = true;
        isValidBirthDate: boolean = true;
        isValidGender: boolean = true;
        genderOptions = [
            { label: 'Male', value: 'MALE' },
            { label: 'Female', value: 'FEMALE' }
        ];
        isValidAddress: boolean = true;
        isValidStreetNumber: boolean = true;
        isValidBoxNumber: boolean = true;
        isValidCity: boolean = true;
        isValidZip: boolean = true;
        isValidPhoneNumber: boolean = true;
        isValidEmail: boolean = true;
        isValidJob: boolean = true;
        isValidHobbies: boolean = true; 
        isValidCountry: boolean = true;


        constructor(
            private fb: FormBuilder,
            private selectionService: SelectionService, 
            private messageService: MessageService,
            private patientService: PatientService,
            private storageService: StorageService,
            private confirmationService: ConfirmationService,
            private router: Router
        ) {}

        
            ngOnInit() {
                this.patient = this.selectionService.getPatient();
        
                if (this.patient) {
                    if (this.patient.birthDate) {
                        this.birthDateValue = new Date(this.patient.birthDate);
                    }
        
                    this.idOrga = this.patient.organizations ? this.patient.organizations.map(org => org.id) : [];
                    if (!this.idOrga.length) {
                        this.idOrga = this.storageService.get('organization') || [];
                    }
        
                    if (!this.patient.address) {
                        this.patient.address = this.createEmptyAddress();
                    }
                } 
            }
        
            createEmptyAddress() {
                return {
                    id: null,
                    street: "",
                    streetNumber: "",
                    boxNumber: "",
                    zipCode: "",
                    city: "",
                    country: "",
                    createdBy: "System",
                    createdAt: new Date().toISOString(),
                    modifiedBy: "System",
                    modifiedAt: new Date().toISOString(),
                    deleted: false,
                    version: 1
                };
            }

            validateInputs() {
                const namePattern = /^[A-zÀ-ú-]+(?:['-\s][A-zÀ-ú-]+)*$/; // Lettres, espaces, tirets, apostrophes
                const ssnPattern = /^\d{2}\.\d{2}\.\d{2}-\d{3}\.\d{2}$/; // SSN format: XX.XX.XX-XXX.XX
                const zipPattern = /^\d{4,5}$/; // Code postal : 4 ou 5 chiffres
                const phonePattern = /^[0-9\s+()-]{8,15}$/; // 8 à 15 chiffres, espaces, +, ()
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Email standard
                const streetNumberPattern = /^[A-z0-9/-]+$/; // Numéro de rue (lettres, chiffres, - et /)
                const boxNumberPattern= /^[A-z0-9/-]*$/; // Box number (facultatif)
                const countryPattern = /^[A-zÀ-ú\s-]+$/; // Pays (lettres et espaces)
                const cityPattern = /^[A-zÀ-ú\s-]+$/; // Ville (lettres et espaces)
                const validGenders = ["MALE", "FEMALE"];
                
            
                // Champs obligatoires
                this.isValidFirstname = this.patient?.firstname ? namePattern.test(this.patient.firstname) : false;
                this.isValidLastname = this.patient?.lastname ? namePattern.test(this.patient.lastname) : false;
                this.isValidSSN = !this.patient?.niss || ssnPattern.test(this.patient.niss);
                this.isValidBirthDate = !!this.patient?.birthDate;
                this.isValidGender = !this.patient?.gender || validGenders.includes(this.patient.gender.toUpperCase());
            
                // Champs optionnels
                this.isValidPhoneNumber = !this.patient?.phone || phonePattern.test(this.patient.phone);
                this.isValidEmail = !this.patient?.mail || emailPattern.test(this.patient.mail);
                this.isValidJob = !this.patient?.job || namePattern.test(this.patient.job);
                this.isValidHobbies = !this.patient?.hobbies || namePattern.test(this.patient.hobbies);
            
                if (this.patient?.address) {
                    this.isValidCity = !this.patient.address.city || cityPattern.test(this.patient.address.city);
                    this.isValidZip = !this.patient.address.zipCode || zipPattern.test(this.patient.address.zipCode);
                    this.isValidAddress = !this.patient.address.street || namePattern.test(this.patient.address.street);
                    this.isValidStreetNumber = !this.patient.address.streetNumber || streetNumberPattern.test(this.patient.address.streetNumber);
                    this.isValidBoxNumber = !this.patient.address.boxNumber || boxNumberPattern.test(this.patient.address.boxNumber);
                    this.isValidCountry = !this.patient.address.country || countryPattern.test(this.patient.address.country);
                }
            }
        
            updatePatient() {
                if (!this.patient || !this.patient.id) {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Patient ID is missing.' });
                    return;
                }
            
                if (!this.idOrga.length) {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'At least one organization ID is required.' });
                    return;
                }
            
                this.validateInputs();
            
                // Vérification des champs obligatoires
                if (!this.isValidFirstname || !this.isValidLastname || !this.isValidSSN || !this.isValidBirthDate) {
                    this.messageService.add({ severity: 'error', summary: 'Validation Error', detail: 'Required fields are invalid.' });
                    return;
                }
            
                // 🛠 Vérification de l'adresse
                if (this.patient.address) {
                    const isAddressEmpty = 
                        !this.patient.address.street?.trim() &&
                        !this.patient.address.streetNumber?.trim() &&
                        !this.patient.address.boxNumber?.trim() &&
                        !this.patient.address.zipCode?.trim() &&
                        !this.patient.address.city?.trim() &&
                        !this.patient.address.country?.trim();
            
                    if (isAddressEmpty) {
                        this.patient.address = null; 
                    }
                }
            
                this.sendUpdateRequest();
            }
            
            
            sendUpdateRequest() {
                this.patientService.updatePatient(this.patient, this.idOrga).subscribe({
                    next: (updatedPatient) => {
            
                        this.isEditMode = false;
                        this.patient = updatedPatient;
            
                        this.messages = [
                            { severity: 'success', detail: `The patient ${updatedPatient.firstname} ${updatedPatient.lastname} has been successfully updated.` }
                        ];
            
                        setTimeout(() => {
                            this.selectionService.resetPatient();
                            this.router.navigate(['/']);
                        }, 2000);
                    },
                    error: (err) => {
                        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update patient.' });
                    }
                });
            }
            
        
        toggleEditMode() {
            if (this.isEditMode) {
                this.patient = JSON.parse(JSON.stringify(this.originalPatient));
            } else {
                this.originalPatient = JSON.parse(JSON.stringify(this.patient));
            }
            this.isEditMode = !this.isEditMode;
        }

        
        /**
         * Supprime un patient après confirmation.
         */
        deletePatient() {
            if (!this.patient || !this.patient.id) {
                this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'No patient selected!' });
                return;
            }
        
            if (!this.idOrga || this.idOrga.length === 0) {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'At least one organization ID is required.' });
                return;
            }
        
            // Afficher la boîte de dialogue de confirmation PrimeNG
            this.confirmationService.confirm({
                message: `Are you sure you want to delete patient ${this.patient.firstname} ${this.patient.lastname}?`,
                header: 'Confirm Deletion',
                icon: 'pi pi-exclamation-triangle',
                accept: () => this.confirmDeletePatient(), // Supprime le patient si "Yes" est cliqué
                reject: () => this.messageService.add({ severity: 'info', summary: 'Cancelled', detail: 'Deletion cancelled.' })
            });
        }
        
        /**
         * Supprime réellement le patient après confirmation
         */
        confirmDeletePatient() {
            if (!this.patient || !this.patient.id) return;
        
            this.patientService.deletePatient(this.patient.id, this.idOrga).subscribe({
                next: () => {
                    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Patient successfully deleted!' });
        
                    this.selectionService.setPatient(null);
                    this.patient = null;
        
                    setTimeout(() => {
                        this.router.navigate(['/']);
                    }, 1500);
                },
                error: () => {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete patient. Please try again.' });
                }
            });
        }
    }    