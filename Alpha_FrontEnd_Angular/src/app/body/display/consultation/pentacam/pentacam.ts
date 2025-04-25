import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './pentacam.html',
    selector: 'pentacam-display',
})
export class PentacamDisplay implements OnInit {
    constructor() {}
    hoa: number;
    z40: number;
    cord: number;
    ngOnInit(): void {}
}
