import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'corrective-prescriptions-display',
    templateUrl: './corrective-prescriptions.html',
})
export class CorrectivePrescriptionsDisplay implements OnInit {
    sphereGlasses: number;
    cylindreGlasses: number;
    axeGlasses: number;
    acvisuGlasses: number;

    sphereLenses: number;
    cylindreLenses: number;
    axeLenses: number;
    acvisuLenses: number;

    sphereEquivalence: number;
    cylindreEquivalence: number;
    axeEquivalence: number;
    acvisuEquivalence: number;

    constructor() {}

    ngOnInit(): void {
    }
}
