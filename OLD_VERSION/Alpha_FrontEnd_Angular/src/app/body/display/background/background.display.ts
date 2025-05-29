import { Component, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { DeviceDetectionService } from '../../service/DeviceDetectionService';

@Component({
  selector: 'background-display',
  templateUrl: './background.display.html',
})
export class BackgroundDisplay implements OnInit {
  isMobileDevice: boolean = false;

  medicalBackgrounds!: TreeNode[];
  selectedMedicalBackgrounds: TreeNode[] = [];

  medicalTreatments!: TreeNode[];
  selectedMedicalTreatments: TreeNode[] = [];

  ophthalmologicalHistorys!: TreeNode[];
  selectedOphthalmologicalHistorys: TreeNode[] = [];

  ophthalmologicalTreatments!: TreeNode[];
  selectedOphthalmologicalTreatments: TreeNode[] = [];

  otherComments: { [key: string]: string } = {};

  constructor(private deviceService: DeviceDetectionService) {}

  ngOnInit() {
    this.isMobileDevice = this.deviceService.isMobile();

    this.medicalBackgrounds = [
      { label: 'None' },
      { label: 'Diabetes' },
      { label: 'Hypertension' },
      { label: 'Renal failure' },
      { label: 'Stroke' },
      { label: 'Other', key: 'MedicalBackground' },
    ];

    this.medicalTreatments = [
      { label: 'None' },
      { label: 'α1 Antagonist' },
      { label: 'Anticoagulants' },
      { label: 'Antiplatelet agents' },
      { label: 'Other', key: 'MedicalTreatment' },
    ];

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
          { label: 'Other', key: 'OphHistoryOD' },
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
            ],
          },
          { label: 'Scar/Trauma' },
          { label: 'Dry eye' },
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
            children: [{ label: 'Dry' }, { label: 'Wet' }],
          },
          { label: 'Dystrophy' },
          { label: 'Vein occlusion' },
          { label: 'Artery occlusion' },
          { label: 'Vitreomacular traction' },
          { label: 'Other', key: 'Vitreo-retinal' },
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
            ],
          },
          { label: 'Other', key: 'Optic nerve' },
        ],
      },
    ];

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

  onNodeSelect(event: { node: TreeNode }, nodes: TreeNode[], selectedNodes: TreeNode[]) {
    const parent = this.findParent(event.node, nodes);
    if (parent && !selectedNodes.includes(parent)) {
      selectedNodes.push(parent);
    }

    if (event.node.label === 'Other' && event.node.key) {
      this.otherComments[event.node.key] = '';
    }
  }

  onNodeUnselect(event: { node: TreeNode }, nodes: TreeNode[], selectedNodes: TreeNode[]) {
    const parent = this.findParent(event.node, nodes);
    if (parent && !this.hasSelectedChildren(parent, selectedNodes)) {
      const index = selectedNodes.indexOf(parent);
      if (index > -1) selectedNodes.splice(index, 1);
    }

    if (event.node.label === 'Other' && event.node.key) {
      delete this.otherComments[event.node.key];
    }
  }

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

  hasSelectedChildren(node: TreeNode, selectedNodes: TreeNode[]): boolean {
    if (!node.children) return false;
    return node.children.some(child => selectedNodes.includes(child));
  }
}
