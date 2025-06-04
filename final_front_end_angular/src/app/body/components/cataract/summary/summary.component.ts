import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService, ConfirmationService, Message } from 'primeng/api';
import { StorageService } from '../../../service/StorageService';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.scss'],
  providers: [DatePipe, MessageService, ConfirmationService]
})
export class SummaryComponent implements OnInit {
  patient: any;
  summary: any = {};
  isEditMode: boolean = false;
  birthDateValue: string | null = null;
  messages: Message[] | undefined;
  idOrga: number[] = [];

  dominantEyeOptions = [
    { label: 'OD', value: 'OD' },
    { label: 'OS', value: 'OS' }
  ];

  anesthesiaOptions = [
    { label: 'General anesthesia', value: 'General anesthesia' },
    { label: 'Topical and sedation', value: 'Topical and sedation' },
    { label: 'Local', value: 'Local' },
    { label: 'Local and sedation', value: 'Local and sedation' },
    { label: 'Topical', value: 'Topical' }
  ];

  surgeryTypeOptions = [
    { label: 'Cataract', value: 'Cataract' },
    { label: 'Refractive', value: 'Refractive' },
    { label: 'Glaucoma', value: 'Glaucoma' }
  ];

  treatmentOptions = ['Collyre 1', 'Collyre 2', 'Autre pré-op'];
  appointmentOptions = ['Day 1', 'Day 5', 'Week 1', 'Week 2', 'Month 1', 'Month 2', 'Month 3', 'Month 6'];
  materialOptions = ['Microscope', 'Phaco', 'Vitrector'];
  surgeonOptions = [
    { name: 'Dr Crahay', value: 'Dr Crahay' },
    { name: 'Dr Jones', value: 'Dr Jones' },
    { name: 'Dr Joe', value: 'Dr Joe' }
  ];

  constructor(
    private route: ActivatedRoute,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private storageService: StorageService,
    private datePipe: DatePipe
  ) {}

 ngOnInit(): void {
  this.patient = this.storageService.get('patient');
  this.idOrga = this.storageService.get('organization');

  if (this.patient?.birthDate) {
    this.birthDateValue = this.datePipe.transform(this.patient.birthDate, 'dd-MM-yyyy');
  }

  // ✅ Enregistre le patient dans le StorageService pour que la topbar le voie sur toutes les pages
  if (this.patient) {
    this.storageService.set('patient', this.patient);
  }

  this.loadSummaryData(this.patient?.workflowId ?? null);
}


  loadSummaryData(id: string | null) {
    // Chargement ou initialisation du résumé opératoire
    this.summary = {
      profession: '',
      atcdO: '',
      medicalHistory: '',
      dominantEye: '',
      allergy: '',
      anesthesia: '',
      surgeryType: '',
      laterality: '',
      dateSurgery: null,
      emergency: '',
      recall: '',
      iol: '',
      visitWithSurgeon: '',
      surgeon: '',
      preopTreatments: [],
      postopTreatments: [],
      postopNotes: '',
      postopAppointments: [],
      operatingMaterials: []
    };
  }

  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
  }

  save() {
    this.toggleEditMode();
    this.messageService.add({
      severity: 'success',
      summary: 'Saved',
      detail: 'Summary has been updated'
    });
  }
}
