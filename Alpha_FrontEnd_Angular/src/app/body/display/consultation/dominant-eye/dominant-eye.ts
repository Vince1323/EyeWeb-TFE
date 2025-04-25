import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './dominant-eye.html',
    selector: 'dominant-eye-display',
})
export class DominantEyeDisplay implements OnInit {
    constructor() {}
    dominant: any[] = [{ label: 'OD', value: 'OD' },{ label: 'OS', value: 'OS' }];
    director: any[] = [{ label: 'OD', value: 'OD' },{ label: 'OS', value: 'OS' }];
    ngOnInit(): void {}
}
