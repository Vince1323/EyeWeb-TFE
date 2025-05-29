import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {Patient} from "../api/patient/Patient";

@Injectable({
    providedIn: 'root',
})
export class SelectionService {
    private patientSource = new BehaviorSubject<Patient | null>(null);

    constructor() {
        const data = sessionStorage.getItem('patient');
        if (data) {
            this.patientSource.next(JSON.parse(data));
        }
    }

    setPatient(patient: Patient): void {
        sessionStorage.setItem('patient', JSON.stringify(patient));
        this.patientSource.next(patient);
    }

    getPatient(): Patient | null {
        const data = sessionStorage.getItem('patient');
        return data ? JSON.parse(data) : null;
    }

    getPatientChanges(): Observable<Patient | null> {
        return this.patientSource.asObservable();
    }

    resetPatient() {
        this.patientSource.next(null);
        sessionStorage.removeItem('patient');
    }
}
