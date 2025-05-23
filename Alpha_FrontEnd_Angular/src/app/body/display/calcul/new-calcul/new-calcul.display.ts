import { Component, EventEmitter, Input, OnInit, Output, QueryList, ViewChildren } from "@angular/core";
import { DatePipe } from "@angular/common";
import { ConfirmationService, MessageService } from "primeng/api";
import { NewCalculForm } from "../new-calcul-form/new-calcul-form";
import { Patient } from "../../../api/patient/Patient";
import { Calcul } from "../../../api/calcul/Calcul";
import { DeviceDetectionService } from "../../../service/DeviceDetectionService";
import { CalculService } from "../../../service/CalculService";

@Component({
    templateUrl: "./new-calcul.display.html",
    selector: "new-calcul-display",
    providers: [DatePipe, MessageService, ConfirmationService],
})
export class NewCalculDisplay implements OnInit {
    @Input() visible: boolean;
    @Input() patient: Patient;
    @Input() idOrga: number[];
    @Output() visibleModified = new EventEmitter<boolean>();
    @Output() closeError = new EventEmitter<any>();
    @ViewChildren(NewCalculForm) newCalculForms!: QueryList<NewCalculForm>;

    calculOD = {} as Calcul;
    calculOS = {} as Calcul;
    calculs: Calcul[];
    loading: boolean = false;
    desabledCalculOD: boolean;
    desabledCalculOS: boolean;
    isScreenLarge: boolean;
    isMobileDevice: boolean = false;
    messageError: Record<string, Record<string, string[]>>;

    selectedSide: string = "OU";
    sideOptions: any[] = [
        { name: "OU", value: "OU" },
        { name: "OD", value: "OD" },
        { name: "OS", value: "OS" },
    ];

    constructor(private messageService: MessageService, private calculService: CalculService, private deviceService: DeviceDetectionService, private confirmationService: ConfirmationService) {}

    ngOnInit() {
        this.desabledCalculOD = this.desabledCalculOS = true;
        this.isMobileDevice = this.deviceService.isMobile();
    }

    selectSide(): void {
        if (!this.selectedSide) {
            this.selectedSide = "OU";
        }
    }

    calculate() {
        this.calculs = [];
        this.messageError = {};
        if (this.newCalculForms) {
            this.newCalculForms.forEach((newCalcul) => {
                this.calculs.push(newCalcul.calculate());
            });
        }
        console.log(this.calculs)
        
        this.confirmConstructPearl(this.calculs);
        this.confirmConstructCooke(this.calculs);
        if (Object.keys(this.messageError).length !== 0) {
            this.confirmationService.confirm({
                header: `Validation Errors`,
                acceptLabel: "OK",
                accept: () => {
                    this.filterFormula();
                },
                reject: () => {
                    this.closeErrorModal();
                },
            });
        } else {
            this.launchFormula();
        }
    }

    filterFormula() {
        let formulaErrorOD: string[] = this.messageError["OD"] ? Object.keys(this.messageError["OD"]) : null;
        let formulaErrorOS: string[] = this.messageError["OS"] ? Object.keys(this.messageError["OS"]) : null;
        this.calculs.forEach((c) => {
            if (c.eyeSide === "OD") {
                c.constants = c.constants.filter((constant) => !formulaErrorOD.includes(constant.formula));
            }
            if (c.eyeSide === "OS") {
                c.constants = c.constants.filter((constant) => !formulaErrorOS.includes(constant.formula));
            }
        });
        this.calculs = this.calculs.filter((c) => c.constants.length > 0);
        this.launchFormula();
    }

    launchFormula() {
        this.loading = true;
        this.calculService.injectionOnEachWebSite(this.calculs, this.idOrga).subscribe({
            next: (resp) => {
                this.loading = false;
                this.visibleModified.emit(false);
            },
            error: (err) => {
                this.loading = false;
                this.closeError.emit(err);
                console.log(err);
            },
        });
    }

    closeErrorModal() {
        this.closeError.emit();
    }

    updateDisabledCalculOD(status: boolean) {
        this.desabledCalculOD = status;
    }

    updateDisabledCalculOS(status: boolean) {
        this.desabledCalculOS = status;
    }

    disabledCalcul() {
        if (this.selectedSide == "OU") {
            return this.desabledCalculOD || this.desabledCalculOS;
        } else if (this.selectedSide == "OD") {
            return this.desabledCalculOD;
        } else {
            return this.desabledCalculOS;
        }
    }

    copyCalcul(side: string) {
        let copyCalcul = {} as Calcul;
        if (this.newCalculForms) {
            this.newCalculForms
                .filter((newCalcul) => newCalcul.calculate().eyeSide === side)
                .forEach((newCalcul) => {
                    copyCalcul = newCalcul.calculate();
                });

            this.newCalculForms.forEach((newCalcul) => newCalcul.getCopy(copyCalcul));
        }
    }

    confirmConstructPearl(calculs: Calcul[]) {
        calculs.forEach((calcul) => {
            const { exam, eyeSide } = calcul;
            let errors: string[] = [];

            if (!exam.al || exam.al < 17 || exam.al > 40) {
                errors.push(`AL (${exam.al}) must be between 17 and 40 mm.`);
            }
            if (!exam.k1 || exam.k1 < 30 || exam.k1 > 60) {
                errors.push(`K1 (${exam.k1}) must be between 30 and 60 diopters.`);
            }
            if (!exam.k2 || exam.k2 < 30 || exam.k2 > 60) {
                errors.push(`K2 (${exam.k2}) must be between 30 and 60 diopters.`);
            }
            if (exam.k1 && exam.k2 && exam.k2 < exam.k1) {
                errors.push(`K2 (${exam.k2}) cannot be smaller than K1 (${exam.k1}).`);
            }
            if (!exam.acd || exam.acd < 1 || exam.acd > 6) {
                errors.push(`ACD (${exam.acd}) must be between 1 and 6 mm.`);
            }
            if (calcul.targetRefraction !== undefined && (calcul.targetRefraction < -4 || calcul.targetRefraction > 1.5)) {
                errors.push(`Target (${calcul.targetRefraction}) must be between -4 and 1.5 diopters.`);
            }
            if (calcul.constants.find((c) => c.formula === "Pearl")) {
                if (
                    calcul.constants.find((c) => c.formula === "Pearl").value === undefined ||
                    parseFloat(calcul.constants.find((c) => c.formula === "Pearl").value) < 116.5 ||
                    parseFloat(calcul.constants.find((c) => c.formula === "Pearl").value) > 121
                ) {
                    errors.push(`A-Constant (${calcul.constants.find((c) => c.formula === "Pearl").value}) must be between 116.5 and 121.`);
                }
            }
            if (errors.length > 0) {
                let formula = calcul.constants.find((c) => c.formula === "Pearl").formula;
                if (!this.messageError[eyeSide]) {
                    this.messageError[eyeSide] = {};
                }
                if (!this.messageError[eyeSide][formula]) {
                    this.messageError[eyeSide][formula] = [];
                }
                this.messageError[eyeSide][formula] = errors;
            }
        });
    }

    confirmConstructCooke(calculs: Calcul[]) {
        calculs.forEach((calcul) => {
            const { exam, eyeSide } = calcul;
            let errors: string[] = [];

            if (!exam.k1 || exam.k1 < 32 || exam.k1 > 57) {
                errors.push(`K1 (${exam.k1}) must be between 32 and 57 diopters.`);
            }
            if (!exam.k2 || exam.k2 < 32 || exam.k2 > 57) {
                errors.push(`K2 (${exam.k2}) must be between 32 and 57 diopters.`);
            }
            if (!exam.al || exam.al < 13 || exam.al > 40) {
                errors.push(`AL (${exam.al}) must be between 13 and 40 mm.`);
            }
            if (!exam.acd || exam.acd < 1.1 || exam.acd > 5.9) {
                errors.push(`ACD (${exam.acd}) must be between 1.1 and 5.9 mm.`);
            }
            if (calcul.targetRefraction !== undefined && (calcul.targetRefraction < -15 || calcul.targetRefraction > 30)) {
                errors.push(`Target (${calcul.targetRefraction}) must be between -15 and 30 diopters.`);
            }
            if (calcul.constants.find((c) => c.formula === "Cooke")) {
                if (
                    calcul.constants.find((c) => c.formula === "Cooke").value === undefined ||
                    parseFloat(calcul.constants.find((c) => c.formula === "Cooke").value) < 101 ||
                    parseFloat(calcul.constants.find((c) => c.formula === "Cooke").value) > 129
                ) {
                    errors.push(`A-Constant (${calcul.constants.find((c) => c.formula === "Cooke").value}) must be between 101 and 129.`);
                }
            }
            if (errors.length > 0) {
                let formula = calcul.constants.find((c) => c.formula === "Cooke")?.formula;
                if (formula) {
                    if (!this.messageError[eyeSide]) {
                        this.messageError[eyeSide] = {};
                    }
                    if (!this.messageError[eyeSide][formula]) {
                        this.messageError[eyeSide][formula] = [];
                    }
                    this.messageError[eyeSide][formula] = errors;
                }
            }
        });
    }
}
