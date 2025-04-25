import { NumberSymbol } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
    templateUrl: './keratometry.html',
    selector: 'keratometry-display',
})
export class KeratometryDisplay implements OnInit {
    constructor() {}
    k1: number;
    k1axe: number;
    astigmatisme: number;
    axe: number;
    k2: number;
    k2axe: number;
    ngOnInit(): void {}
}
