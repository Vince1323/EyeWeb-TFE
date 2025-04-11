import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message, TreeNode } from 'primeng/api';

@Component({
    templateUrl: './vitreous.html',
    selector: 'vitreous-display',
    styleUrls: ['./vitreous.scss']
})
export class VitreousDisplay implements OnInit {
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
                    { label: 'Vitrectomy' },
                    { label: 'Weiss Ring' },
                    { label: 'Floaters' },
                    { label: 'Vitreitis' },
                    { label: 'Hemorrhage' },
                    { label: 'Other' },
                ],
            },
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
