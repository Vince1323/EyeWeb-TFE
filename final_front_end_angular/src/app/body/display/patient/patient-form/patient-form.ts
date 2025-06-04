import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PatientService } from "../../../service/PatientService";
import { Patient } from "../../../api/patient/Patient";

@Component({
    templateUrl: "./patient-form.html",
    selector: "patient-form",
})
export class PatientForm implements OnInit {
    constructor(private fb: FormBuilder, private patientService: PatientService) {}

    @Input() patient: Patient;
    @Input() isSelectedOrga: boolean = false;
    @Input() importModalOCR: boolean = false;
    @Output() patientModified = new EventEmitter<Patient>();
    patientForm: FormGroup;

    genders: any[];
    minDate: Date = new Date("01-01-1900");
    maxDate: Date = new Date();
    date: any;
    loading: boolean = false;

    ngOnInit(): void {
        if (this.patient.birthDate) {
            const dateTemp = new Date(this.patient.birthDate);
            const day = dateTemp.getDate();
            const month = dateTemp.getMonth();
            const year = dateTemp.getFullYear();
            this.date = new Date(year, month, day);
        }
        this.getGender();
        this.initializeForm();
    }

    initializeForm() {
        this.patientForm = this.fb.group({
            firstname: [this.patient.firstname, [Validators.required, Validators.pattern("[A-zÀ-ú-]+(?:['-\\s][[A-zÀ-ú-]+)*")]],
            lastname: [this.patient.lastname, [Validators.required, Validators.pattern("[A-zÀ-ú-]+(?:['-\\s][[A-zÀ-ú-]+)*")]],
            phone: [this.patient.phone],
            mail: [this.patient.mail],
            niss: [this.patient.niss],
            gender: [this.patient.gender, [Validators.required]],
            birthDate: [this.date, [Validators.required]],
            job: [this.patient.job],
            hobbies: [this.patient.hobbies],
        });
    }

    getGender() {
        this.patientService.getGender().subscribe({
            next: (res) => {
                this.genders = res.gender;
            },
        });
    }

    save() {
        const formValues = this.patientForm.value;
        if (formValues.birthDate instanceof Date) {
            formValues.birthDate = this.formatDate(formValues.birthDate);
        }
        this.patientModified.emit(formValues);
    }

    formatDate(date: Date): string {
        const day = date.getDate().toString().padStart(2, "0");
        const month = (date.getMonth() + 1).toString().padStart(2, "0");
        const year = date.getFullYear();
        return `${day}-${month}-${year}`;
    }

    onPatientModified() {
        this.patientModified.emit(this.patientForm.value);
    }
}
