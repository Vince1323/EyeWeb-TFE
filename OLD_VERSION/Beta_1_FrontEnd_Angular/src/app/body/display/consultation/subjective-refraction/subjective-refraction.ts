import { NumberSymbol } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './subjective-refraction.html',
    selector: 'subjective-refraction-display',
})
export class SubjectiveRefractionDisplay implements OnInit {
    constructor() {}
    sphere: number;
    cylindre: number;
    axe: number;
    addition: number;
    acvisu: number;
    avpres: number;
    avbinoc: number;
    ngOnInit(): void {}
}
