import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Message, TreeNode } from 'primeng/api';

@Component({
    templateUrl: './cornea.html',
    selector: 'cornea-display',
    styleUrls: ['./cornea.scss']
})
export class CorneaDisplay implements OnInit {
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
                        label: 'Fuchs dystrophy/Guttata',
                    },
                    {
                        label: 'Keratoconus',
                        children: [
                            {
                                label: 'Fleischer ring'
                            },
                            {
                                label: 'Vogts striae'
                            },
                            {
                                label: 'Nebula'
                            },
                            {
                                label: 'Hydrops'
                            }
                        ]
                    },
                    {
                        label: 'Dystrophia',
                        children: [
                            {
                                label: 'Endothelial'
                            },
                            {
                                label: 'Stromal'
                            },
                            {
                                label: 'Epithelial'
                            }
                        ]
                    },

                    {
                        label: 'Graft',
                        children: [
                            {
                                label: 'Penetrating keratoplasty'
                            },
                            {
                                label: 'DSAEK'
                            },
                            {
                                label: 'DMEK'
                            },
                            {
                                label: 'DALK'
                            }
                        ],
                    },
                    {
                        label: 'Pterygium',
                    },
                    {
                        label: 'Salzmann s nodular degeneration',
                    },
                    {
                        label: 'Peripheral degeneration',
                    },
                    {
                        label: 'Radial keratotomy(nb of incisions)',
                    },
                    {
                        label: 'Trauma',
                    },
                    {
                        label: 'Nebula / Haze',
                        children: [
                            {
                                label: 'Location'
                                // add textarea
                            },

                        ],
                    },
                    {
                        label: 'Staining uptake'
                    },
                    {
                        label: 'Other'
                    }
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
