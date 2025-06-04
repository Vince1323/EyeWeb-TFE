import { Component, OnInit } from '@angular/core';
import { Planning } from 'src/app/body/api/operation/Planning';
import { PlanningService } from '../../../service/PlanningService';
import { MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-planning',
  templateUrl: './planning.component.html',
  styleUrls: ['./planning.component.scss'],
  providers: [MessageService],
})
export class PlanningComponent implements OnInit {
  patientId: number; // patientId récupéré dynamiquement via URL
  planningList: Planning[] = [];
  selectedPlanning: Planning | null = null;
  planningDialog: boolean = false;

  // Initialisation de la structure d'un nouveau planning
  newPlanning: Planning = {
    id: 0,
    planningDate: '',
    eyeSide: 'OD',
    eyeDescription: '',
    notes: '',
    patientId: 0, // patientId doit être dynamique
    createdBy: '',
    createdAt: '',
    modifiedBy: '',
    modifiedAt: '',
  };

  constructor(
    private planningService: PlanningService,
    private messageService: MessageService,
    private route: ActivatedRoute // Récupérer l'ID du patient depuis l'URL
  ) {}

  ngOnInit(): void {
    // Récupération dynamique de l'ID du patient depuis l'URL
    this.route.paramMap.subscribe(params => {
      this.patientId = +params.get('patientId')!; // Assurez-vous que l'ID est un nombre
      this.loadPlannings();
    });
  }

  loadPlannings() {
    // Charger les plannings en fonction du patientId
    this.planningService.getAllByPatient(this.patientId).subscribe((data) => {
      this.planningList = data;
    });
  }

  openNew() {
    // Réinitialisation du planning pour en créer un nouveau
    this.newPlanning = {
      id: 0,
      planningDate: '',
      eyeSide: 'OD',
      eyeDescription: '',
      notes: '',
      patientId: this.patientId, // Utilisation du patientId dynamique
      createdBy: '',
      createdAt: '',
      modifiedBy: '',
      modifiedAt: '',
    };
    this.planningDialog = true;
  }

  savePlanning() {
    // Appel au service pour créer ou mettre à jour le planning
    const serviceCall = this.newPlanning.id === 0
      ? this.planningService.create(this.newPlanning)
      : this.planningService.update(this.newPlanning);

    serviceCall.subscribe({
      next: () => {
        this.loadPlannings(); // Recharge les plannings après ajout/modification
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Planning saved' });
        this.planningDialog = false; // Ferme la fenêtre de dialogue
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'An error occurred' });
      }
    });
  }

  editPlanning(planning: Planning) {
    // Prépare les données pour l'édition
    this.newPlanning = { ...planning };
    this.planningDialog = true;
  }

  deletePlanning(planning: Planning) {
    // Supprimer le planning et recharger la liste
    this.planningService.delete(planning.id).subscribe({
      next: () => {
        this.loadPlannings();
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: 'Planning deleted' });
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete planning' });
      }
    });
  }
}
