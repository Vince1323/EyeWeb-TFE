import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { DatePipe, formatDate } from "@angular/common";
import { MessageService } from "primeng/api";
import { Patient } from "../../../api/patient/Patient";
import { Exam } from "../../../api/exam/Exam";
import { CataractService } from "../../../service/CataractService";

@Component({
    templateUrl: "./average.html",
    selector: "average-display",
    providers: [],
})
export class AverageDisplay implements OnInit {
    @Input() visible!: boolean;
    @Input() side!: string;
    @Input() patient!: Patient;
    @Input() idOrga!: number[];
    @Output() visibleModified = new EventEmitter<boolean>();

    index!: number;
    biometries: Exam[] = [];
    selectedBiometries: Exam[] = [];
    average!: Exam;

    steps = [
        {
            label: "Selection",
        },
        {
            label: "Loading",
        },
        {
            label: "Result",
        },

        {
            label: "Finish",
        },
    ];

    constructor(private messageService: MessageService, private cataractService: CataractService) {}

    ngOnInit() {
        this.index = 0;
        if (this.side == "OD") {
            this.cataractService.getAllExamsByPatientId(this.patient.id, this.idOrga, "OD").subscribe({
                next: (biometries) => {
                    this.biometries = biometries;
                },
            });
        } else if (this.side == "OS") {
            this.cataractService.getAllExamsByPatientId(this.patient.id, this.idOrga, "OS").subscribe({
                next: (biometries) => {
                    this.biometries = biometries;
                },
            });
        }
    }

    close() {
        this.visibleModified.emit(false);
    }

    calculate() {
        this.index = 1;
        this.cataractService.calculAverage(this.patient.id, this.idOrga, this.selectedBiometries).subscribe({
            next: (biometry) => {
                this.average = biometry;
                this.average.patientId = this.patient.id;
                this.index = 2;
                console.log(this.average);
            },
        });
    }

    save() {
        this.average.calculDate = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss", "fr-FR");
        this.average.importType = "Average";
        this.cataractService.addExam(this.average, this.idOrga).subscribe({
            next: (biometry) => {
                this.index = 3;
                this.visibleModified.emit(false);
            },
        });
    }
}
