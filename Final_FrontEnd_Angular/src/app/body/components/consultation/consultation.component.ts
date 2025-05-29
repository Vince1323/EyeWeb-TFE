import { Component, OnInit } from '@angular/core';
import { SelectionService } from '../../service/SelectionService';

@Component({
  selector: 'app-consultation',
  templateUrl: './consultation.component.html'
})
export class ConsultationComponent implements OnInit {
  patient: any;
  isEditMode = false;

  consultation = {
    date: null,
    reason: '',
    symptoms: '',
    comments: ''
  };

  constructor(private selectionService: SelectionService) {}

  ngOnInit(): void {
    this.patient = this.selectionService.getPatient(); // ✅ Important !
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  save(): void {
    // Sauvegarde future
    this.isEditMode = false;
  }
}
