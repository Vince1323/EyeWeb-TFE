import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message, TreeNode } from 'primeng/api';

@Component({
    templateUrl: './anterior-chamber.html',
    selector: 'anterior-chamber-display',
    styleUrls: ['./anterior-chamber.scss'],
})
export class AnteriorChamberDisplay implements OnInit {
    constructor() {}
    normal!: TreeNode[];
    selectedNodes: TreeNode | TreeNode[] = [];
    abnormalLabels: string[] = [];

    ngOnInit(): void {
        this.normal = [
            { label: 'Normal'
            },
            {label: 'Abnormal',
                children: [
                    {
                        label: 'Pseudoexfoliation',
                    },
                    {
                        label: 'Pigment dispersion',
                    },
                    {
                        label: 'Iridotomy',
                        children: [
                            {
                                label: 'Location'
                            }
                        ],
                    },
                    {
                        label: 'Shallow',
                    },
                    {
                        label: 'Uveitis/uveitis sequelae',
                    },
                    {
                        label: 'Coloboma/Iris defect',
                    },
                    {
                        label: 'Other ',
                    },
                ],
            },
        ]
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
