import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './objective-refraction.html',
    selector: 'objective-refraction-display',
})
export class ObjectiveRefractionDisplay implements OnInit {
    constructor() {}
    sphere: number ;
    cylindre: number ;
    axe: number ;
    ngOnInit(): void {}
}
