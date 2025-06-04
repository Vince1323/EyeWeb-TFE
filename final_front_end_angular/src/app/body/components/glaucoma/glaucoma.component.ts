import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SelectionService } from '../../service/SelectionService';

@Component({
  selector: 'app-glaucoma',
  templateUrl: './glaucoma.component.html'
})
export class GlaucomaComponent implements OnInit {
  isEditMode = false;
  patient: any;
  birthDateValue!: Date;

  glaucomaData = {
    pressureOD: '',
    pressureOS: '',
    visualFieldOD: '',
    visualFieldOS: '',
    comment: ''
  };

  constructor(private route: ActivatedRoute, private selectionService: SelectionService) {}

  ngOnInit(): void {
    this.patient = this.selectionService.getPatient();
    if (this.patient?.birthDate) {
      this.birthDateValue = new Date(this.patient.birthDate);
    }
    console.log('[Glaucoma] Patient sélectionné :', this.patient);
  }

  toggleEdit(): void {
    this.isEditMode = !this.isEditMode;
  }

  save(): void {
    this.isEditMode = false;
    console.log('[Glaucoma] Données enregistrées :', this.glaucomaData);
  }
}
