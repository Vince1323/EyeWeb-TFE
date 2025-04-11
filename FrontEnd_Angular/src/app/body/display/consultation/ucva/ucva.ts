import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './ucva.html',
    selector: 'ucva-display',
})
export class UcvaDisplay implements OnInit {
    constructor() {}
    avloin: number;
    avinter: number;
    avpres: number;
    ngOnInit(): void {}
}
