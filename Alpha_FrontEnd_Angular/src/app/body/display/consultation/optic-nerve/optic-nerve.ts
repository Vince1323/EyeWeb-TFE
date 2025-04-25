import { Component, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';

@Component({
    templateUrl: './optic-nerve.html',
    selector: 'optic-nerve-display',
    styleUrls: ['./optic-nerve.scss'],
})
export class OpticNerveDisplay implements OnInit {
    normal!: TreeNode[];
    selectedNodes: TreeNode | TreeNode[] = [];
    abnormalLabels: string[] = [];

    ngOnInit(): void {
        this.normal = [
            { label: 'Normal' },
            {
                label: 'Abnormal',
                data: 'Abnormal',
                children: [
                    { label: 'Glaucoma', children: [{ label: 'Excavation' }] },
                    { label: 'Hemorrhage' },
                    { label: 'Naturopathy' }
                ],
            },
        ];
    }

    checkAbnormal(): void {
        const selectedArray = Array.isArray(this.selectedNodes) ? this.selectedNodes : [this.selectedNodes];

        // Filtrer uniquement les éléments sous "Abnormal"
        this.abnormalLabels = selectedArray
            .filter(node => this.isUnderAbnormal(node))
            .map(node => node.label);
    }

    isUnderAbnormal(node: TreeNode): boolean {
        return this.normal[1].children?.some(child => child.label === node.label) ?? false;
    }
}
