import { Component, OnInit } from '@angular/core';
import { SelectionService } from '../../service/SelectionService';

@Component({
  selector: 'app-refractive',
  templateUrl: './refractive.component.html'
})
export class RefractiveComponent implements OnInit {
  isEditMode = false;
  patient: any;
  birthDateValue: Date | null = null;

  refractiveData = {
    visualAcuityOD: '',
    visualAcuityOS: '',
    prescriptionOD: '',
    prescriptionOS: '',
    comments: ''
  };

  constructor(private selectionService: SelectionService) {}

  ngOnInit(): void {
    this.patient = this.selectionService.getPatient();
    if (this.patient?.birthDate) {
      this.birthDateValue = new Date(this.patient.birthDate);
    }
  }

  toggleEdit() {
    this.isEditMode = !this.isEditMode;
  }

  save() {
    this.isEditMode = false;
    console.log('Saved refractive data:', this.refractiveData);
  }
}
