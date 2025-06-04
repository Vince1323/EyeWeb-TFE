import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './specular.html',
    selector: 'specular-display',
})
export class SpecularDisplay implements OnInit {
    constructor() {}
    specular: number;
    ngOnInit(): void {}
}
