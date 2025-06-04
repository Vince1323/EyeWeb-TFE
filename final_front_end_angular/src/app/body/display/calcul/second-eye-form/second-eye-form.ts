import { Component, Input, OnInit } from "@angular/core";
import { AuthService } from "../../../service/AuthService";
import { DeviceDetectionService } from "../../../service/DeviceDetectionService";
import { StorageService } from "../../../service/StorageService";
import { CalculService } from "../../../service/CalculService";
import { CataractService } from "../../../service/CataractService";
import { DatePipe } from "@angular/common";
import { Exam } from "src/app/body/api/exam/Exam";
import { ExamSummary } from "src/app/body/api/exam/ExamSummary";
import { CalculSummary } from "src/app/body/api/calcul/CalculSummary";
import { CalculMatrix } from "src/app/body/api/calcul/CalculMatrix";
import { Calcul } from "src/app/body/api/calcul/Calcul";

@Component({
    templateUrl: "./second-eye-form.html",
    selector: "second-eye-form",
    providers: [AuthService, CataractService, StorageService, DatePipe],
})
export class SecondEyeForm implements OnInit {
    patient = this.storageService.get("patient");
    idOrga: number[] = this.storageService.get("organization");

    @Input() sideSecondEye: string;
    @Input() currentCalcul: Calcul;

    biometries: ExamSummary[] = [];
    selectedBiometry = {} as Exam;
    selectedBiometryTable = [];
    lightExam!: ExamSummary;
    calculs: CalculSummary[];
    lightCalcul: CalculSummary;
    selectedCalcul: CalculMatrix;
    powerWrong: boolean = false;
    seWrong: boolean = false;
    se: number;
    AconstantPearl: string;

    isMobileDevice: boolean = false;
    
    constructor(
        private storageService: StorageService,
        private calculService: CalculService,
        private deviceService: DeviceDetectionService,
        private cataractService: CataractService,
        private datePipe: DatePipe
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        this.sideSecondEye = this.sideSecondEye == "OD" ? "OS" : "OD";
        this.currentCalcul.se = 0;
        this.cataractService.getAllExamsByPatientIdEyeSideCalculPowerTrue(this.patient.id, this.idOrga, this.sideSecondEye).subscribe({
            next: (biometries) => {
                const formattedBiometries = biometries.map((biometry) => ({
                    ...biometry,
                    calculDate: this.datePipe.transform(biometry.calculDate, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.biometries = formattedBiometries;
                if (formattedBiometries.length > 0) {
                    this.lightExam = formattedBiometries[0];
                    this.selectBiometry(formattedBiometries[0].id);
                }
            },
        });
    }

    selectBiometry(id: number) {
        this.cataractService.getExamById(id, this.idOrga).subscribe({
            next: (biometry) => {
                this.selectedBiometry = biometry;
                this.selectedBiometryTable[0] = this.selectedBiometry;
                this.currentCalcul.idReferencePrecExam = id;
                this.selectCalculSummary(id);
            },
        });
    }

    selectCalculSummary(id: number) {
        this.calculService.getAllCalculsByExamIdEyeSidePowerTrue(id, this.idOrga, this.sideSecondEye).subscribe({
            next: (calculs) => {
                const formattedCalculs = calculs.map((calcul) => ({
                    ...calcul,
                    createdAt: this.datePipe.transform(calcul.createdAt, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.calculs = formattedCalculs;
                if (formattedCalculs.length > 0) {
                    this.lightCalcul = formattedCalculs[0];
                    this.selectCalcul(formattedCalculs[0].id);
                }
            }
        });
    }

    selectCalcul(id: number) {
        this.calculService.getCalculById(id, this.idOrga).subscribe({
            next: (calcul) => {
                this.selectedCalcul = calcul;
                this.AconstantPearl = this.selectedCalcul.constants.filter(row => row.formula == 'Pearl')[0].value;
                this.currentCalcul.idReferencePrecCalcul = this.selectedCalcul.id;
                this.currentCalcul.precPowerSelected = this.selectedCalcul.selectedPower;
                this.selectedCalcul.valueMatrix = this.selectedCalcul.valueMatrix.filter(row => row[0] == this.selectedCalcul.selectedPower);
                // Vérification uniquement pour contraintes de PEARL
                this.powerWrong = this.selectedCalcul.selectedPower < 0.0 || this.selectedCalcul.selectedPower > 35.0;
            }
        });
    }

    checkSE() {
        if (this.currentCalcul.se < -9 || this.currentCalcul.se > 9 || isNaN(this.currentCalcul.se) || !this.currentCalcul.se) {
            this.currentCalcul.se = 0;
            this.seWrong = true;
        } else {
            this.seWrong = false;
        }
    }
}
