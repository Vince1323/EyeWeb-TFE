import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './remarks.html',
    selector: 'remarks-display',
})
export class RemarksDisplay implements OnInit {
    constructor() {}
    remarks: number;
    ngOnInit(): void {}
}
