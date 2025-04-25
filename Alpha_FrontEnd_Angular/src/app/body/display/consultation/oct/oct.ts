import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import {TreeNode} from "primeng/api";

@Component({
    templateUrl: './oct.html',
    selector: 'oct-display',
})
export class OctDisplay implements OnInit {
    constructor() {}
    normal!: TreeNode[];
    ngOnInit(): void {
        this.normal = [
            { label: 'Normal'
            },
            {label: 'Abnormal'}
        ];
    }
}
