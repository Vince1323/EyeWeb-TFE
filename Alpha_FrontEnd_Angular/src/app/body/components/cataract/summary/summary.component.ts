import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-summary',
  templateUrl: './summary.component.html',
  styleUrls: ['./summary.component.scss'],
  providers: [MessageService, ConfirmationService]
})
export class SummaryComponent implements OnInit {
  patient: any;
  summary: any = {};
  isEditMode: boolean = false;
  birthDateValue: Date | null = null;

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
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('idPatient');
    this.loadPatient(id);
    this.loadSummaryData(id);
  }

  loadPatient(id: string | null) {
    this.patient = {
      id,
      firstname: 'Romain',
      lastname: 'Chariot'
    };
    this.birthDateValue = new Date('1999-01-01');
  }

  loadSummaryData(id: string | null) {
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
