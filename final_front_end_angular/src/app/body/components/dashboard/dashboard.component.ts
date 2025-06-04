import { Component, OnInit } from '@angular/core';
import { PatientService } from '../../service/PatientService';
import { MachineService } from '../../service/MachineService';
import { OcrService } from '../../service/OcrService';
import { Message, MessageService } from 'primeng/api';
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
  providers: [PatientService, MachineService, AuthService, OcrService, MessageService]
})
export class DashboardComponent implements OnInit {

  // Connected user and organization context
  user = this.authService.getUserInfoFromToken();
  idOrga: number[] = this.storageService.get('organization');

  // Main patient list and total count for pagination
  patients: Patient[] = [];
  totalRecords = 0;

  // Selected patient from table
  selectedPatient!: Patient;

  // UI state
  loadingPatients = false;
  isMobileDevice = false;
  searchTriggered = false;

  // Search field
  searchText = '';

  // Pagination
  first = 0;
  rows = 10;

  // Modals
  importExcel = false;
  importPDF = false;
  importEncode = false;

  private patientSubscription!: Subscription;

  constructor(
    public layoutService: LayoutService,
    private patientService: PatientService,
    private authService: AuthService,
    private selectionService: SelectionService,
    private storageService: StorageService,
    private deviceService: DeviceDetectionService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.isMobileDevice = this.deviceService.isMobile();

    // Restore selected patient if one was selected previously
    if (this.selectionService.getPatient() !== null) {
      this.selectedPatient = this.selectionService.getPatient();
    }

    // Listen to selection changes from other components
    this.patientSubscription = this.selectionService
      .getPatientChanges()
      .subscribe((selectedPatient) => {
        this.selectedPatient = selectedPatient;
      });
  }

  /**
   * Triggered when user clicks on the Search button
   */
  onSearch() {
    if (this.searchText.trim().length === 0) return;

    this.searchTriggered = true;
    this.first = 0;
    this.loadPatients(0, this.rows);
  }

  /**
   * Loads filtered patients from the backend using pagination
   */
  loadPatients(page: number, size: number) {
    this.loadingPatients = true;

    this.patientService.getFilteredPatients(this.idOrga, this.searchText, page, size).subscribe({
      next: (res) => {
        this.patients = res.content;
        this.totalRecords = res.totalElements;
        this.loadingPatients = false;
      },
      error: () => {
        this.loadingPatients = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load patients.',
        });
      }
    });
  }

  /**
   * Loads all patients from the organization without filtering or pagination
   */
  showAllPatients() {
    this.loadingPatients = true;
    this.searchTriggered = true;
    this.searchText = '';
    this.first = 0;

    this.patientService.getPatientsByOrganizationId(this.idOrga).subscribe({
      next: (patients) => {
        this.patients = patients;
        this.totalRecords = patients.length;
        this.loadingPatients = false;
      },
      error: () => {
        this.loadingPatients = false;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load all patients.'
        });
      }
    });
  }

  /**
   * Pagination change event handler
   */
  pageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;

    const page = event.first / event.rows;
    if (this.searchTriggered) {
      this.loadPatients(page, this.rows);
    }
  }

  /**
   * Reset the search field and table
   */
  resetSearch() {
    this.searchText = '';
    this.searchTriggered = false;
    this.patients = [];
    this.totalRecords = 0;
    this.first = 0;
  }

  /**
   * Selects a patient and navigates to their biometric page
   */
  selectPatient(id: number) {
    this.loadingPatients = true;

    this.patientService.getPatientById(id, this.idOrga).subscribe({
      next: (patient) => {
        patient.fullname = `${patient.firstname} ${patient.lastname} (${patient.birthDate})`;
        this.selectionService.setPatient(patient);
        this.router.navigate([NAVIGATION.patientBiometrics(patient.id)]);
      },
      error: () => {
        this.loadingPatients = false;
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Patient not found.' });
      },
    });
  }

  // Modals handlers
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
