import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message, TreeNode } from 'primeng/api';


@Component({
    templateUrl: './conjunctiva.html',
    selector: 'conjunctiva-display',
    styleUrl: './conjunctiva.scss',
})
export class ConjunctivaDisplay implements OnInit {
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
                        label: 'Injection',
                        children: [
                            {
                                label: '1'
                            },
                            {
                                label: '2'
                            },
                            {
                                label: '3'
                            },
                            {
                                label: '4'
                            },
                            {
                                label: '5'
                            }
                        ]
                    },{
                        label: 'Staining uptake',
                        children: [
                            {
                                label: '1'
                            },
                            {
                                label: '2'
                            },
                            {
                                label: '3'
                            },
                            {
                                label: '4'
                            },
                            {
                                label: '5'
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
