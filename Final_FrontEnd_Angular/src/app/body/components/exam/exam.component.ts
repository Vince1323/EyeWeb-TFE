import { Component, OnInit } from '@angular/core';
import { SelectionService } from '../../service/SelectionService';

@Component({
  selector: 'app-exam',
  templateUrl: './exam.component.html'
})
export class ExamComponent implements OnInit {
  patient: any;
  isEditMode = false;

  exam = {
    visualAcuityOD: '',
    visualAcuityOS: '',
    intraocularPressureOD: '',
    intraocularPressureOS: '',
    comments: ''
  };

  constructor(private selectionService: SelectionService) {}

  ngOnInit(): void {
    this.patient = this.selectionService.getPatient(); // 🔁 essentiel
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  save(): void {
    // logique de sauvegarde future
    this.isEditMode = false;
  }
}
