import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import {TreeNode} from "primeng/api";

@Component({
    templateUrl: './retina.html',
    selector: 'retina-display',
    styleUrl: './retina.scss',
})
export class RetinaDisplay implements OnInit {
    constructor() {}
    normal!: TreeNode[];
    selectedNodes: TreeNode | TreeNode[] = [];
    abnormalLabels: string[] = [];

    ngOnInit(): void {
        this.normal = [
            { label: 'Normal' },
            {
                label: 'Abnormal',
                children: [
                    { label: 'AMD (Age-related Macular Degeneration)' },
                    { label: 'Diabetic Retinopathy' },
                    { label: 'Epiretinal Membrane' },
                    { label: 'Vitreomacular Traction' },
                    { label: 'CSCR Sequelae (Central Serous Chorioretinopathy)' },
                    { label: 'CRVO (Central Retinal Vein Occlusion)' },
                    { label: 'CRAO (Central Retinal Artery Occlusion)' },
                    {
                        label: 'Macular Edema',
                        children: [
                            { label: 'Diabetes' },
                            { label: 'AMD (Age-related Macular Degeneration)' },
                            { label: 'BRVO (Branch Retinal Vein Occlusion)' },
                            { label: 'Other' }
                        ]
                    },
                    { label: 'Other' }
                ]
            }
        ];
    }

    checkAbnormal(): void {
        const selectedArray = Array.isArray(this.selectedNodes) ? this.selectedNodes : [this.selectedNodes];

        this.abnormalLabels = selectedArray
            .filter(node => this.isUnderAbnormal(node))
            .map(node => node.label);
    }

    isUnderAbnormal(node: TreeNode): boolean {
        return this.normal[1].children?.some(child => child.label === node.label) ?? false;
    }
}
