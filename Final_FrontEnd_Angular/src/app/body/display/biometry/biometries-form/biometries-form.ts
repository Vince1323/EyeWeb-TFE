import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { formatDate } from '@angular/common';
import {Biometry} from "../../../api/exam/Biometry";
import {SelectionService} from "../../../service/SelectionService";
import {DeviceDetectionService} from "../../../service/DeviceDetectionService";
import { Exam } from 'src/app/body/api/exam/Exam';

@Component({
    templateUrl: './biometries-form.html',
    selector: 'biometries-form',
    providers: [],
})
export class BiometriesForm implements OnInit {
    constructor(
        private fb: FormBuilder,
        private selectionService: SelectionService,
        private deviceService: DeviceDetectionService
    ) {}

    @Input() biometries!: Exam[];
    @Input() isAddNewBio: boolean = false;
    @Output() biometryModified = new EventEmitter<Biometry[]>();
    @Output() goBackPatient = new EventEmitter();

    //VAR RESPONSIVE
    isMobileDevice: boolean = false;

    biometryOD = {} as Exam;
    biometryOS = {} as Exam;
    biometryForm!: FormGroup;
    isFormsValid: boolean = false;
    seletedPatient: boolean = false;
    loading: boolean = false;

    ngOnInit(): void {
        this.isMobileDevice = this.deviceService.isMobile();
        this.seletedPatient = this.selectionService.getPatient() ? true : false;
        if (this.biometries) {
            if (this.biometries[0].eyeSide === 'OD') {
                this.biometryOD = this.biometries[0];
                if (this.biometries[1]) {
                    this.biometryOS = this.biometries[1];
                }
            } else {
                this.biometryOS = this.biometries[0];
            }
        }
        this.initializeForm();
        this.checkFormValidity();
    }

    initializeForm(): void {
        this.biometryForm = this.fb.group({
            biometryOD: this.fb.group({
                k1: [this.biometryOD.k1],
                k1Axis: [this.biometryOD.k1Axis],
                k2: [this.biometryOD.k2],
                k2Axis: [this.biometryOD.k2Axis],
                al: [this.biometryOD.al],
                acd: [this.biometryOD.acd],
                kAvg: [this.biometryOD.kAvg],
                kAstig: [this.biometryOD.kAstig],
                wtw: [this.biometryOD.wtw],
                pupilDia: [this.biometryOD.pupilDia],
                cord: [this.biometryOD.cord],
                snr: [this.biometryOD.snr],
                z40: [this.biometryOD.z40],
                cct: [this.biometryOD.cct],
                lensThickness: [this.biometryOD.lensThickness],
                machine: [this.biometryOD.machine],
                importType: [this.biometryOD.importType],
            }),
            biometryOS: this.fb.group({
                k1: [this.biometryOS.k1],
                k1Axis: [this.biometryOS.k1Axis],
                k2: [this.biometryOS.k2],
                k2Axis: [this.biometryOS.k2Axis],
                al: [this.biometryOS.al],
                acd: [this.biometryOS.acd],
                kAvg: [this.biometryOS.kAvg],
                kAstig: [this.biometryOS.kAstig],
                wtw: [this.biometryOS.wtw],
                pupilDia: [this.biometryOS.pupilDia],
                cord: [this.biometryOS.cord],
                snr: [this.biometryOS.snr],
                z40: [this.biometryOS.z40],
                cct: [this.biometryOS.cct],
                lensThickness: [this.biometryOS.lensThickness],
                machine: [this.biometryOS.machine],
                importType: [this.biometryOS.importType],
            }),
        });
    }

    checkFormValidity() {
        const validityOD = this.checkValidity(
            this.biometryForm.get('biometryOD') as FormGroup
        );
        const validityOS = this.checkValidity(
            this.biometryForm.get('biometryOS') as FormGroup
        );

        // Cas où les deux formulaires sont entièrement vides
        if (validityOD.isEmpty && validityOS.isEmpty) {
            this.isFormsValid = false;
            return;
        }

        // Cas où au moins un formulaire est partiellement rempli mais pas totalement valide
        if (
            (validityOD.isPartiallyFilled && !validityOD.isFullyValid) ||
            (validityOS.isPartiallyFilled && !validityOS.isFullyValid)
        ) {
            this.isFormsValid = false;
            return;
        }

        // Cas où les formulaires sont soit entièrement valides, et/ou l'un est entièrement vides
        this.isFormsValid = true;
    }

    checkValidity(form: FormGroup): {
        isEmpty: boolean;
        isFullyValid: boolean;
        isPartiallyFilled: boolean;
    } {
        const values = form.value;
        const requiredFields = ['k1', 'k2', 'al', 'acd'];
        const allFieldsEmpty = Object.keys(values).every((key) => !values[key]);
        const isPartiallyFilled = Object.keys(values).some(
            (key) => !!values[key]
        );
        const requiredFieldsFilled = requiredFields.every(
            (field) => !!values[field]
        );

        return {
            isEmpty: allFieldsEmpty,
            isFullyValid: requiredFieldsFilled,
            isPartiallyFilled: isPartiallyFilled && !allFieldsEmpty,
        };
    }

    saveBiometry() {
        this.biometryOD = this.biometryForm.get('biometryOD')?.value;
        if (this.biometryOD.k1) {
            this.biometryOD.eyeSide = 'OD';
            this.biometryOD.selected = true;
            this.biometryOD.calculDate = formatDate(
                new Date(),
                'yyyy-MM-dd HH:mm:ss',
                'fr-FR'
            );
            if (
                this.biometryOD.importType === undefined ||
                this.biometryOD.importType === null ||
                this.biometryOD.importType === ''
            ) {
                this.biometryOD.importType = 'Manual';
            }
            if (!this.biometryOD.machine) {
                this.biometryOD.machine = 'MANUAL ENCODING';
            }
        }
        this.biometryOS = this.biometryForm.get('biometryOS')?.value;
        if (this.biometryOS.k1) {
            this.biometryOS.eyeSide = 'OS';
            this.biometryOS.selected = true;
            this.biometryOS.calculDate = formatDate(
                new Date(),
                'yyyy-MM-dd HH:mm:ss',
                'fr-FR'
            );
            if (
                this.biometryOS.importType === undefined ||
                this.biometryOS.importType === null ||
                this.biometryOD.importType === ''
            ) {
                this.biometryOS.importType = 'Manual';
            }
            if (!this.biometryOS.machine) {
                this.biometryOS.machine = 'MANUAL ENCODING';
            }
        }
        this.load();
        this.biometryModified.emit([this.biometryOD, this.biometryOS]);
    }

    get biometryOSFormGroup(): FormGroup {
        return (
            (this.biometryForm.get('biometryOS') as FormGroup) ||
            this.fb.group({})
        );
    }

    get biometryODFormGroup(): FormGroup {
        return (
            (this.biometryForm.get('biometryOD') as FormGroup) ||
            this.fb.group({})
        );
    }

    goBack() {
        this.saveBiometry();
        this.goBackPatient.emit();
    }

    load() {
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 5000);
    }
}
