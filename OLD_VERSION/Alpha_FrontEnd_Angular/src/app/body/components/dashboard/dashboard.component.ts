import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { PatientService } from '../../service/PatientService';
import { MachineService } from '../../service/MachineService';
import { OcrService } from '../../service/OcrService';
import { Message, MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { AuthService } from '../../service/AuthService';
import { LayoutService} from "../../../layout/service/app.layout.service";
import { SelectionService } from '../../service/SelectionService';
import { StorageService} from "../../service/StorageService";
import { DeviceDetectionService } from '../../service/DeviceDetectionService';
import { Router} from "@angular/router";
import { NAVIGATION } from 'src/app/constants/app.constants';
import {Patient} from "../../api/patient/Patient";

@Component({
    templateUrl: './dashboard.component.html',
    providers: [
        PatientService,
        MachineService,
        AuthService,
        OcrService,
    ],
})
export class DashboardComponent implements OnInit {
    user = this.authService.getUserInfoFromToken();
    idOrga: number[] = this.storageService.get('organization');
    patients!: Patient[];
    selectedPatient!: any;
    loadingPatients: boolean = false;
    messages: Message[] | undefined;
    isMobileDevice: boolean = false;

    importExcel: boolean = false;
    importPDF: boolean = false;
    importEncode: boolean = false;

    first = 0;
    rows = 10;

    searchQuery: string = ''; 

    private patientSubscription!: Subscription;

    constructor(
        public layoutService: LayoutService,
        private patientService: PatientService,
        private authService: AuthService,
        private selectionService: SelectionService,
        private storageService: StorageService,
        private deviceService: DeviceDetectionService,
        private router: Router,
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.loadingPatients = true;
        this.patientService.getPatientsByOrganizationId(this.idOrga).subscribe({
            next: (patients) => {
                this.patients = patients;
                this.loadingPatients = false;
            },
            error: (err) => {
                this.loadingPatients = false;
            },
        });
        if (this.selectionService.getPatient() !== null) {
            this.selectedPatient = this.selectionService.getPatient();
        }

        this.patientSubscription = this.selectionService
            .getPatientChanges()
            .subscribe((selectedPatient) => {
                this.selectedPatient = selectedPatient;
            });
    }

    next() {
        this.first = this.first + this.rows;
    }

    prev() {
        this.first = this.first - this.rows;
    }

    reset() {
        this.first = 0;
    }

    pageChange(event) {
        this.first = event.first;
        this.rows = event.rows;
    }

    isLastPage(): boolean {
        return this.patients ? this.first === this.patients.length - this.rows : true;
    }

    isFirstPage(): boolean {
        return this.patients ? this.first === 0 : true;
    }

    searchPatients() {
        if (this.searchQuery.trim()) {
            // Filtre les patients par nom
            this.patients = this.patients.filter(patient =>
                patient.lastname.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                patient.firstname.toLowerCase().includes(this.searchQuery.toLowerCase())
            );
        } else {
            // Si aucune recherche, affiche tous les patients
            this.patients = [...this.patients];
        }
    }
      
    selectPatient(id: number) {
        this.patientService.getPatientById(id, this.idOrga).subscribe({
            next: (patient) => {
                patient.fullname = `${patient.firstname} ${patient.lastname} (${patient.birthDate})`;
                this.selectionService.setPatient(patient);
                this.router.navigate([NAVIGATION.patientBiometrics(patient.id)]);
            },
            error: (err) => {
                this.loadingPatients = false;
            },
        });
    }

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
