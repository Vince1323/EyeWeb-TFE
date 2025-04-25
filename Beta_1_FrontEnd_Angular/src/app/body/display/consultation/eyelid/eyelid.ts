import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message, TreeNode } from 'primeng/api';


@Component({
    templateUrl: './eyelid.html',
    selector: 'eyelid-display',
    styleUrl: './eyelid.scss',
})
export class EyelidDisplay implements OnInit {
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
                        label: 'Entropion',
                    },
                    {
                        label: 'Ectropion',
                    },
                    {
                        label: 'Ptosis',
                    },
                    {
                        label: 'Anterior blepharitis',
                        children: [
                            {
                                label: 'Staphylococcus'
                            },
                            {
                                label: 'Demodex'
                            }
                        ]
                    },
                    {
                        label: 'Other',
                    },
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
