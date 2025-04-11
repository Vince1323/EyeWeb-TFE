import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './pupillometry.html',
    selector: 'pupillometry-display',
})
export class PupillometryDisplay implements OnInit {
    constructor() {}
    min: number;
    max: number;
    ngOnInit(): void {}
}
