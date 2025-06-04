import { Component, OnInit } from '@angular/core';
import { SelectionService } from '../../../service/SelectionService';
import { StorageService } from '../../../service/StorageService';

@Component({
  selector: 'app-planning',
  templateUrl: './planning.component.html'
})
export class PlanningComponent implements OnInit {
  patient: any;
  isEditMode = false;
  birthDateValue: Date | null = null;

  planning = {
    surgeryDate: null,
    anesthesia: null,
    surgeryType: null,
    notesOD: '',
    notesOS: '',
    comment: ''
  };

  anesthesiaOptions = [
    { label: 'Topical', value: 'TOPICAL' },
    { label: 'Peribulbar', value: 'PERIBULBAR' },
    { label: 'General', value: 'GENERAL' }
  ];

  surgeryTypeOptions = [
    { label: 'Cataract', value: 'CATARACT' },
    { label: 'Refractive', value: 'REFRACTIVE' },
    { label: 'Combined', value: 'COMBINED' }
  ];

  constructor(
    private selectionService: SelectionService,
    private storageService: StorageService 
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
    // logique de sauvegarde
    this.isEditMode = false;
  }
}
