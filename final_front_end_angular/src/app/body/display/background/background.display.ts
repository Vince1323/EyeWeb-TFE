import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { DeviceDetectionService } from '../../service/DeviceDetectionService';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'background-display',
  templateUrl: './background.display.html',
  encapsulation: ViewEncapsulation.None
})
export class BackgroundDisplay implements OnInit {
  // Détermine si l'utilisateur est sur un appareil mobile
  isMobileDevice: boolean = false;

  // Données de l'historique médical et traitements
  medicalBackgrounds!: TreeNode[];
  selectedMedicalBackgrounds: TreeNode[] = [];

  medicalTreatments!: TreeNode[];
  selectedMedicalTreatments: TreeNode[] = [];

  // Données de l'historique ophtalmologique et traitements
  ophthalmologicalHistorys!: TreeNode[];
  selectedOphthalmologicalHistorys: TreeNode[] = [];

  ophthalmologicalTreatments!: TreeNode[];
  selectedOphthalmologicalTreatments: TreeNode[] = [];

  // Dictionnaire pour stocker les commentaires associés aux noeuds "Other"
  otherComments: { [key: string]: string } = {};

  constructor(
    private deviceService: DeviceDetectionService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.isMobileDevice = this.deviceService.isMobile();

    // Initialisation des options d'antécédents médicaux
    this.medicalBackgrounds = [
      { label: 'None' },
      { label: 'Diabetes' },
      { label: 'Hypertension' },
      { label: 'Renal failure' },
      { label: 'Stroke' },
      { label: 'Other', key: 'MedicalBackground' },
    ];

    // Traitements médicaux disponibles
    this.medicalTreatments = [
      { label: 'None' },
      { label: 'α1 Antagonist' },
      { label: 'Anticoagulants' },
      { label: 'Antiplatelet agents' },
      { label: 'Other', key: 'MedicalTreatment' },
    ];

    // Historique ophtalmologique avec enfants imbriqués
    this.ophthalmologicalHistorys = [
      { label: 'None' },
      {
        label: 'Refractive surgery',
        children: [
          { label: 'LASIK myopia' },
          { label: 'PRK myopia' },
          { label: 'Radial keratotomy' },
          { label: 'PTK' },
          { label: 'ICL' },
          { label: 'LASIK hypermetropia' },
          { label: 'PRK hypermetropia' },
          { label: 'Smile' },
          { label: 'Other', key: 'OphHistory_RefractiveOther' },
        ],
      },
      {
        label: 'Glaucoma',
        children: [
          { label: 'Chronic open-angle' },
          { label: 'Chronic closed-angle' },
          { label: 'Acute closed-angle' },
          { label: 'Pseudoexfoliative' },
          { label: 'Pigmentary' },
          {
            label: 'Surgery',
            children: [
              { label: 'Trabeculectomy' },
              { label: 'MIGS' },
              { label: 'Valve' },
              { label: 'Other', key: 'OphHistory_GlaucomaSurgeryOther' },
            ],
          },
        ],
      },
      { label: 'Amblyopia' },
      {
        label: 'Cornea',
        children: [
          { label: 'Cornea guttata' },
          {
            label: 'Dystrophy',
            children: [
              { label: 'Epithelial' },
              { label: 'Stromal' },
              { label: 'Posterior' },
              { label: 'Other', key: 'OphHistory_CorneaDystrophyOther' },
            ],
          },
          { label: 'Keratoconus' },
          {
            label: 'Graft',
            children: [
              { label: 'Penetrating' },
              { label: 'DALK' },
              { label: 'DMEK' },
              { label: 'DSAEK' },
              { label: 'Other', key: 'OphHistory_CorneaGraftOther' },
            ],
          },
          { label: 'Scar/Trauma' },
          { label: 'Dry eye' },
          { label: 'Other', key: 'OphHistory_CorneaOther' },
        ],
      },
      {
        label: 'Vitreo-retinal',
        children: [
          { label: 'Retinal detachment' },
          { label: 'Epiretinal membrane' },
          { label: 'Macular hole' },
          { label: 'Diabetic retinopathy' },
          {
            label: 'AMD',
            children: [
              { label: 'Dry' },
              { label: 'Wet' },
              { label: 'Other', key: 'OphHistory_AMDOther' },
            ],
          },
          { label: 'Dystrophy' },
          { label: 'Vein occlusion' },
          { label: 'Artery occlusion' },
          { label: 'Vitreomacular traction' },
          { label: 'Other', key: 'OphHistory_VitreoOther' },
        ],
      },
      {
        label: 'Optic nerve',
        children: [
          {
            label: 'Optic neuropathy',
            children: [
              { label: 'Ischemic anterior' },
              { label: 'Posterior' },
              { label: 'Other', key: 'OphHistory_OpticNeuropathyOther' },
            ],
          },
          { label: 'Other', key: 'OphHistory_OpticNerveOther' },
        ],
      },
    ];

    // Traitements ophtalmologiques disponibles
    this.ophthalmologicalTreatments = [
      { label: 'None' },
      { label: 'Artificial tears' },
      {
        label: 'Anti-glaucoma',
        children: [
          { label: 'Beta-blocker' },
          { label: 'Carbonic anhydrase inhibitor' },
          { label: 'α2-agonist' },
          { label: 'Prostaglandin' },
        ],
      },
      { label: 'Anti-allergic' },
      { label: 'NSAIDs' },
      { label: 'Corticosteroids' },
      { label: 'Other', key: 'OphTreatmentOD' },
    ];
  }

  // Lorsqu'un noeud est sélectionné
  onNodeSelect(event: { node: TreeNode }, nodes: TreeNode[], selectedNodes: TreeNode[]) {
    const node = event.node;

    // Ajout explicite du noeud si non présent
    if (!selectedNodes.includes(node)) {
      selectedNodes.push(node);
    }

    const parent = this.findParent(node, nodes);
    if (parent && !selectedNodes.includes(parent)) {
      selectedNodes.push(parent);
    }

    // Préparation d'un champ commentaire si "Other" est sélectionné
    if (node.label === 'Other' && node.key) {
      this.otherComments[node.key] = '';
    }
  }

  // Lorsqu'un noeud est désélectionné
  onNodeUnselect(event: { node: TreeNode }, nodes: TreeNode[], selectedNodes: TreeNode[]) {
    const node = event.node;

    const index = selectedNodes.indexOf(node);
    if (index !== -1) {
      selectedNodes.splice(index, 1);
    }

    const parent = this.findParent(node, nodes);
    if (parent && !this.hasSelectedChildren(parent, selectedNodes)) {
      const parentIndex = selectedNodes.indexOf(parent);
      if (parentIndex !== -1) {
        selectedNodes.splice(parentIndex, 1);
      }
    }

    // Suppression du commentaire si "Other" est désélectionné
    if (node.label === 'Other' && node.key) {
      delete this.otherComments[node.key];
    }
  }

  // Trouver le parent d'un noeud donné
  findParent(node: TreeNode, nodes: TreeNode[]): TreeNode | null {
    for (const n of nodes) {
      if (n.children?.includes(node)) return n;
      if (n.children) {
        const parent = this.findParent(node, n.children);
        if (parent) return parent;
      }
    }
    return null;
  }

  // Vérifie si un noeud parent a encore des enfants sélectionnés
  hasSelectedChildren(node: TreeNode, selectedNodes: TreeNode[]): boolean {
    if (!node.children) return false;
    return node.children.some(child => selectedNodes.includes(child));
  }

  // Vérifie si un "Other" est sélectionné
  isOtherSelected(key: string, selectedNodes: TreeNode[]): boolean {
    return selectedNodes.some(node => node.key === key);
  }

  // Récupère les clés des noeuds "Other" sélectionnés
  getSelectedOtherKeys(selectedList: TreeNode[]): string[] {
    return selectedList
      .filter(node => node.label === 'Other' && !!node.key)
      .map(node => node.key as string);
  }

  // Sauvegarde une note associée à un noeud "Other"
  saveNote(key: string): void {
    const note = this.otherComments[key]?.trim();
    if (!note) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Empty note',
        detail: 'Please enter a note before saving.'
      });
      return;
    }

    this.messageService.add({
      severity: 'success',
      summary: 'Note saved',
      detail: 'Note for "${key}" saved successfully.'
    });

    delete this.otherComments[key];
  }

  // Sauvegarde globale de toutes les sélections et commentaires
  saveOtherComments(): void {
    const payload = {
      comments: this.otherComments,
      selections: {
        medicalBackgrounds: this.selectedMedicalBackgrounds,
        medicalTreatments: this.selectedMedicalTreatments,
        ophthalmologicalHistorys: this.selectedOphthalmologicalHistorys,
        ophthalmologicalTreatments: this.selectedOphthalmologicalTreatments
      }
    };
    console.log('Saving full payload:', payload);
  }

  // Sauvegarde et suppression de "Other" après saisie du commentaire
  saveAndHideOther(key: string, selection: TreeNode[]) {
    const note = this.otherComments[key]?.trim();

    if (!note) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Warning',
        detail: 'Note is empty.',
      });
      return;
    }

    this.messageService.add({
      severity: 'success',
      summary: 'Saved',
      detail: 'Note has been saved.',
    });

    setTimeout(() => {
      const index = selection.findIndex(n => n.key === key);
      if (index !== -1) {
        selection.splice(index, 1);
      }
    }, 0);
  }
}
