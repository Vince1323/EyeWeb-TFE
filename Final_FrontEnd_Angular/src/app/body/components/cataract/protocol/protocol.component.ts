import { Component, OnInit } from '@angular/core';
import { SelectionService } from '../../../service/SelectionService';
import { StorageService } from 'src/app/body/service/StorageService';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-protocol',
  templateUrl: './protocol.component.html'
})
export class ProtocolComponent implements OnInit {
  patient: any;
  isEditMode = false;
  birthDateValue: Date | null = null;

  protocol = {
    surgeryType: null,
    anesthesia: null,
    eye: null,
    incision: '',
    technique: '',
    iolModel: '',
    materials: '',
    comment: ''
  };

  surgeryTypes = [
    { label: 'Cataract', value: 'CATARACT' },
    { label: 'Refractive', value: 'REFRACTIVE' },
    { label: 'Combined', value: 'COMBINED' }
  ];

  anesthesiaTypes = [
    { label: 'Topical', value: 'TOPICAL' },
    { label: 'Peribulbar', value: 'PERIBULBAR' },
    { label: 'General', value: 'GENERAL' }
  ];

  eyes = [
    { label: 'OD – Right Eye', value: 'OD' },
    { label: 'OS – Left Eye', value: 'OS' },
    { label: 'OU – Both Eyes', value: 'OU' }
  ];

  constructor(
    private selectionService: SelectionService,
    private storageService: StorageService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.patient = this.storageService.get('patient');
    if (this.patient?.birthDate) {
      this.birthDateValue = new Date(this.patient.birthDate);
    }
  }
  
  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  save(): void {
   
    this.isEditMode = false;
  }
}
