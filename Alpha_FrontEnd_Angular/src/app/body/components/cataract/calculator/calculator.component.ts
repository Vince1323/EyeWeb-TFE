import { Component, OnInit } from "@angular/core";
import { LayoutService } from "../../../../layout/service/app.layout.service";
import { AuthService } from "../../../service/AuthService";
import { DeviceDetectionService } from "../../../service/DeviceDetectionService";
import { StorageService } from "../../../service/StorageService";
import { CalculService } from "../../../service/CalculService";
import { Message, MessageService } from "primeng/api";
import { DatePipe } from "@angular/common";
import { CataractService } from "../../../service/CataractService";

@Component({
    templateUrl: "./calculator.component.html",
    providers: [AuthService, CataractService, StorageService, DatePipe, DeviceDetectionService],
})
export class CalculatorComponent implements OnInit {
    user = this.authService.getUserInfoFromToken();
    patient = this.storageService.get("patient");
    idOrga: number[] = this.storageService.get("organization");

    //VAR RESPONSIVE
    isMobileDevice: boolean = false;

    //VAR CALCULS
    empty: Message[] | undefined;

    calculOD: boolean = false;
    calculsOD: any[] = [];
    selectedCalculOD!: any;
    lightOD!: any;

    calculOS: boolean = false;
    calculsOS: any[] = [];
    selectedCalculOS!: any;
    lightOS!: any;

    //VAR NEW CALCUL

    newCalcul: boolean = false;
    targetOD: string;
    targetOS: string;

    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private storageService: StorageService,
        private calculService: CalculService,
        private biometryService: CataractService,
        private datePipe: DatePipe,
        private messageService: MessageService,
        private deviceService: DeviceDetectionService
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.empty = [
            {
                severity: "error",
                detail: "You have not selected any biometrics. Please import and select biometrics first.",
            },
        ];

        this.calculService.getAllCalculsByPatientId(this.patient.id, this.idOrga, "OD").subscribe({
            next: (calculs) => {
                const formattedCalculs = calculs.map((calcul) => ({
                    ...calcul,
                    createdAt: this.datePipe.transform(calcul.createdAt, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.calculsOD = formattedCalculs;
                if (formattedCalculs.length > 0) {
                    this.lightOD = formattedCalculs[0];
                    this.selectCalcul(formattedCalculs[0].id, "OD");
                }
            },
        });
        this.calculService.getAllCalculsByPatientId(this.patient.id, this.idOrga, "OS").subscribe({
            next: (calculs) => {
                const formattedCalculs = calculs.map((calcul) => ({
                    ...calcul,
                    createdAt: this.datePipe.transform(calcul.createdAt, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.calculsOS = formattedCalculs;
                if (formattedCalculs.length > 0) {
                    this.lightOS = formattedCalculs[0];
                    this.selectCalcul(formattedCalculs[0].id, "OS");
                }
            },
        });
        this.biometryService.getAllExamsListByPatientId(this.patient.id, this.idOrga, "OD", true).subscribe({
            next: (biometries) => {
                if (biometries.length > 0) {
                    this.calculOD = true;
                }
            },
        });
        this.biometryService.getAllExamsListByPatientId(this.patient.id, this.idOrga, "OS", true).subscribe({
            next: (biometries) => {
                if (biometries.length > 0) {
                    this.calculOS = true;
                }
            },
        });
    }

    showNewCalcul() {
        this.newCalcul = true;
    }

    hideNewCalcul(visible: boolean) {
        this.newCalcul = visible;
        location.reload();
    }

    closeWithError(error: any) {
        this.newCalcul = false;
        if (error) {
            this.messageService.add({
                severity: "error",
                summary: error.status,
                detail: error.error.message,
            });
        }
    }

    selectCalcul(id: number, side: "OD" | "OS") {
        this.calculService.getCalculById(id, this.idOrga).subscribe({
            next: (calcul) => {
                if (side == "OD") {
                    this.selectedCalculOD = calcul;
                    this.targetOD = "Target: " + this.selectedCalculOD.targetRefraction;
                } else if (side == "OS") {
                    this.selectedCalculOS = calcul;
                    this.targetOS = "Target: " + this.selectedCalculOS.targetRefraction;
                }
            },
        });
    }
}
