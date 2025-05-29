import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { formatDate } from '@angular/common';
import { DeviceDetectionService } from '../../service/DeviceDetectionService';
import { Exam } from '../../api/exam/Exam';
import { Patient } from '../../api/patient/Patient';

@Component({
    templateUrl: './confirmation-display.html',
    selector: 'confirmation-display',
    providers: [DeviceDetectionService],
})
export class ConfirmationDisplay implements OnInit {
    constructor(private deviceService: DeviceDetectionService) {}

    @Input() patient!: Patient;
    @Input() biometries!: Exam[];
    @Output() savePatientBiometries = new EventEmitter();
    @Output() goBackBiometry = new EventEmitter();

    //VAR RESPONSIVE
    isMobileDevice: boolean = false;

    loading: boolean = false;
    biometryOD!: Exam;
    biometryOS!: Exam;

    ngOnInit(): void {
        this.isMobileDevice = this.deviceService.isMobile();
        this.patient.birthDate = formatDate(
            this.patient.birthDate,
            'yyyy-MM-dd',
            'en-US'
        );
        this.biometryOD = this.biometries[0];
        this.biometryOS = this.biometries[1];
    }

    goBack() {
        this.goBackBiometry.emit();
    }

    save() {
        this.savePatientBiometries.emit();
        this.load();
    }

    load() {
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 5000);
    }
}
