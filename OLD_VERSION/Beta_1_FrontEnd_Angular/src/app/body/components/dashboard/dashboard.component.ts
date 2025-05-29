import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../service/PatientService';
import { MachineService } from '../../service/MachineService';
import { OcrService } from '../../service/OcrService';
import { Message } from 'primeng/api';
import { Subscription } from 'rxjs';
import { AuthService } from '../../service/AuthService';
import { LayoutService } from "../../../layout/service/app.layout.service";
import { SelectionService } from '../../service/SelectionService';
import { StorageService } from "../../service/StorageService";
import { DeviceDetectionService } from '../../service/DeviceDetectionService';
import { Router } from "@angular/router";
import { NAVIGATION } from 'src/app/constants/app.constants';
import { Patient } from "../../api/patient/Patient";

@Component({
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    providers: [
        PatientService,
        MachineService,
        AuthService,
        OcrService,
    ],
})
export class DashboardComponent implements OnInit {

    // Infos utilisateur
    user = this.authService.getUserInfoFromToken();
    idOrga: number[] = this.storageService.get('organization');

    // Données patients
    patients: Patient[] = [];
    filteredPatients: Patient[] = [];
    selectedPatient!: Patient;

    // Flags
    loadingPatients = false;
    isMobileDevice = false;
    messages: Message[] | undefined;

    // Dialogues
    importExcel = false;
    importPDF = false;
    importEncode = false;

    // Pagination
    first = 0;
    rows = 10;

    // Filtres dynamiques
    searchLastname = '';
    searchFirstname = '';
    searchBirthdate = '';

    private patientSubscription!: Subscription;

    constructor(
        public layoutService: LayoutService,
        private patientService: PatientService,
        private authService: AuthService,
        private selectionService: SelectionService,
        private storageService: StorageService,
        private deviceService: DeviceDetectionService,
        private router: Router
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.loadingPatients = true;

        // Chargement initial
        this.patientService.getPatientsByOrganizationId(this.idOrga).subscribe({
            next: (patients) => {
                this.patients = patients;
                this.filteredPatients = [...patients];
                this.loadingPatients = false;
            },
            error: () => {
                this.loadingPatients = false;
            },
        });

        // Patient déjà sélectionné ?
        if (this.selectionService.getPatient() !== null) {
            this.selectedPatient = this.selectionService.getPatient();
        }

        // Souscription aux changements
        this.patientSubscription = this.selectionService
            .getPatientChanges()
            .subscribe((selectedPatient) => {
                this.selectedPatient = selectedPatient;
            });
    }

    // 👉 Méthode de filtrage personnalisée
    filterPatients() {
        const last = this.searchLastname.toLowerCase();
        const first = this.searchFirstname.toLowerCase();
        const birth = this.searchBirthdate.toLowerCase();

        this.filteredPatients = this.patients.filter(patient =>
            (!last || patient.lastname?.toLowerCase().includes(last)) &&
            (!first || patient.firstname?.toLowerCase().includes(first)) &&
            (!birth || patient.birthDate?.toLowerCase().includes(birth))
        );
    }

    // 👉 Pagination
    next() {
        this.first = this.first + this.rows;
    }

    prev() {
        this.first = this.first - this.rows;
    }

    reset() {
        this.first = 0;
    }

    pageChange(event: any) {
        this.first = event.first;
        this.rows = event.rows;
    }

    isLastPage(): boolean {
        return this.patients ? this.first >= (this.patients.length - this.rows) : true;
    }

    isFirstPage(): boolean {
        return this.first === 0;
    }

    // 👉 Sélection de patient
    selectPatient(id: number) {
        this.patientService.getPatientById(id, this.idOrga).subscribe({
            next: (patient) => {
                patient.fullname = `${patient.firstname} ${patient.lastname} (${patient.birthDate})`;
                this.selectionService.setPatient(patient);
                this.router.navigate([NAVIGATION.patientBiometrics(patient.id)]);
            },
            error: () => {
                this.loadingPatients = false;
            },
        });
    }

    // 👉 Fermeture des dialogues
    hideImportExcel(visible: boolean) {
        this.importExcel = visible;
    }

    hideImportPDF(visible: boolean) {
        this.importPDF = visible;
    }

    hideImportEncode(visible: boolean) {
        this.importEncode = visible;
    }
}
