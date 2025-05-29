import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import {TreeNode} from "primeng/api";

@Component({
    templateUrl: './conclusion.html',
    selector: 'conclusion-display',
})
export class ConclusionDisplay implements OnInit {
    constructor() {}
    anesthesia!: TreeNode[];
    operation!: TreeNode[];
    selectedNodes: TreeNode | TreeNode[] = [];
    abnormalLabels: string[] = [];
    ngOnInit(): void {
        this.anesthesia = [
            { label: 'No' },
            {
                label: 'Yes',
                children: [
                    { label: 'General Anesthesia (GA)' },
                    { label: 'GA + Sedation' },
                    { label: 'Local Anesthesia (LA)' },
                    { label: 'LA + Sedation' }
                ]
            }
        ];

        this.operation = [
            { label: 'No' },
            {
                label: 'Yes',
                children: [
                    {
                        label: 'OD',
                        children: [
                            {
                                label: 'Cataract',
                                children: [
                                    { label: 'Monofocal' },
                                    { label: 'Enhance Monofocal' },
                                    { label: 'Multifocal Refractive' },
                                    { label: 'Multifocal Diffractive' }
                                ]
                            },
                            { label: 'Clear Lens' },
                            {
                                label: 'Refractive Surgery',
                                children: [
                                    { label: 'LASIK' },
                                    { label: 'PRK' },
                                    { label: 'SMILE' },
                                    { label: 'ICL' }
                                ]
                            },
                            { label: 'Pterygium' },
                            {
                                label: 'Corneal Transplant',
                                children: [
                                    { label: 'DMEK' },
                                    { label: 'DALK' },
                                    { label: 'PKP' },
                                    { label: 'DSAEK' }
                                ]
                            },
                            {
                                label: 'Conjunctival Resection',
                                children: [
                                    { label: 'Conjunctivochalasis' },
                                    { label: 'Superior Limbic Keratitis' }
                                ]
                            },
                            { label: 'IOL Removal' }
                        ]
                    },
                    {
                        label: 'OS',
                        children: [
                            {
                                label: 'Cataract',
                                children: [
                                    { label: 'Monofocal' },
                                    { label: 'Enhance Monofocal' },
                                    { label: 'Multifocal Refractive' },
                                    { label: 'Multifocal Diffractive' }
                                ]
                            },
                            { label: 'Clear Lens' },
                            {
                                label: 'Refractive Surgery',
                                children: [
                                    { label: 'LASIK' },
                                    { label: 'PRK' },
                                    { label: 'SMILE' },
                                    { label: 'ICL' }
                                ]
                            },
                            { label: 'Pterygium' },
                            {
                                label: 'Corneal Transplant',
                                children: [
                                    { label: 'DMEK' },
                                    { label: 'DALK' },
                                    { label: 'PKP' },
                                    { label: 'DSAEK' }
                                ]
                            },
                            {
                                label: 'Conjunctival Resection',
                                children: [
                                    { label: 'Conjunctivochalasis' },
                                    { label: 'Superior Limbic Keratitis' }
                                ]
                            },
                            { label: 'IOL Removal' }
                        ]
                    }
                ]
            }
        ];
    }
}
