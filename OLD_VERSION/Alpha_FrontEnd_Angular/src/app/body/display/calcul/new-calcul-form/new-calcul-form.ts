import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { AutoCompleteCompleteEvent } from "primeng/autocomplete";
import { DatePipe } from "@angular/common";
import { MessageService } from "primeng/api";
import { ConfirmationService } from "primeng/api";
import { Patient } from "../../../api/patient/Patient";
import { Calcul } from "../../../api/calcul/Calcul";
import { Biometry } from "../../../api/exam/Biometry";
import { LensManufacturer } from "../../../api/LensManufacturer";
import { Lens } from "../../../api/Lens";
import { Constant } from "../../../api/Constant";
import { CataractService } from "../../../service/CataractService";
import { LensService } from "../../../service/LensService";
import { LensManufacturerService } from "../../../service/LensManufacturerService";

@Component({
    templateUrl: "./new-calcul-form.html",
    selector: "new-calcul-form",
    providers: [DatePipe, MessageService, ConfirmationService],
})
export class NewCalculForm implements OnInit, AfterViewInit {
    @Input() patient: Patient;
    @Input() calcul: Calcul;
    @Input() idOrga: number[];
    @Input() side: string;
    @Input() sideDisplaySecondEye: string;
    @Input() desabledCalcul: boolean;
    @Output() disabledStatusChange = new EventEmitter<boolean>();

    formulas: string[] = ['Pearl', 'Cooke']; //['Pearl', "Cooke", 'Kane', 'HofferQST', 'EVO', 'Barrett', 'HofferQ', 'Holladay', 'SRKT', 'Haigis'];
    biometries: Biometry[] = [];
    selectedBiometry!: Biometry;
    light!: Biometry;

    manufacturers: LensManufacturer[] = [];
    filteredManufacturers: LensManufacturer[] = [];
    selectedManufacturer!: LensManufacturer;

    lenses!: any[];
    filteredLenses!: any[];
    selectedLens!: Lens;
    targetRefraction!: number;

    isConstants: boolean = false;
    constantType: string = "Manufacturer constant";
    constantsTypes: string[];
    constants: Constant[];
    allAConstant: number = null;
    disabledFormula: boolean = false; 

    typePearl!: string;
    constantPearl!: number;

    typeKane!: string;
    constantKane!: number;

    typeHofferQst!: string;
    constantHofferQst!: number;

    typeEvo!: string;
    constantEvo!: number;

    typeBarrett!: string;
    constantBarrett!: number;

    typeHofferQ!: string;
    constantHofferQ!: number;

    typeHolladay!: string;
    constantHolladay!: number;

    typeSrkt!: string;
    constantSrkt!: number;

    typeCooke!: string;
    constantCooke!: number;

    typeHaigis!: string;
    constantA0!: number;
    constantA1!: number;
    constantA2!: number;

    constructor(
        private biometryService: CataractService,
        private lensService: LensService,
        private lensManufacturerService: LensManufacturerService,
        private datePipe: DatePipe,
        private messageService: MessageService,
        private cdr: ChangeDetectorRef,
        private confirmationService: ConfirmationService
    ) {}

    ngOnInit() {
        this.constantsTypes = ["Manufacturer constant", "Optimized constant", "Custom constant"];
        this.biometryService.getAllExamsByPatientId(this.patient.id, this.idOrga, this.side, true).subscribe({
            next: (biometries) => {
                const formattedBiometries = biometries.map((biometry) => ({
                    ...biometry,
                    calculDate: this.datePipe.transform(biometry.calculDate, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.biometries = formattedBiometries;
                if (formattedBiometries.length > 0) {
                    this.light = formattedBiometries[0];
                    this.selectBiometry(formattedBiometries[0].id);
                }
            }
        });
        this.lensManufacturerService.allLenseManufacturers().subscribe({
            next: (resp) => {
                this.manufacturers = resp;
            }
        });
    }

    ngAfterViewInit() {
        this.notifyParentOfChange();
    }

    filterManufacturer(event: AutoCompleteCompleteEvent) {
        const query = event.query.toLowerCase();
        this.filteredManufacturers = this.manufacturers.filter((manufacturer) => manufacturer.name.toLowerCase().includes(query));
    }

    filterLens(event: AutoCompleteCompleteEvent) {
        const query = event.query.toLowerCase();
        this.filteredLenses = this.lenses.filter((lens) => lens.name.toLowerCase().includes(query));
    }

    importLenses() {
        if(this.selectedManufacturer.id) {
            this.lensService.allLensesByManufacturer(this.selectedManufacturer.id).subscribe({
                next: (resp) => {
                    this.lenses = resp;
                }
            });
            this.disabledCalcul();
        }
    }

    updateAllConstants() {
        this.updateConstantBarrett();
        this.updateConstantEvo();
        this.updateConstantHaigis();
        this.updateConstantHofferQ();
        this.updateConstantHofferQST();
        this.updateConstantHolladay();
        this.updateConstantKane();
        this.updateConstantPearl();
        this.updateConstantSrkt();
        this.updateConstantCooke();
    }

    updateConstants() {
        this.selectConstantType();
        /*
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Astigmatism more than 1.5 dioptries, do you confirm a spherical IOL?',
            header: 'Confirmation',
            icon: 'pi pi-exclamation-triangle',
            acceptIcon:"none",
            rejectIcon:"none",
            rejectButtonStyleClass:"p-button-text",
            accept: () => {
                this.messageService.add({ severity: 'info', summary: 'Confirmed', detail: 'You have accepted' });
            },
            reject: () => {
                this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected', life: 3000 });
            }
        });
        */
        this.constantPearl = this.selectedLens.nominal;
        this.constantKane = this.selectedLens.nominal;
        this.constantHofferQst = this.selectedLens.hofferQPACD;
        this.constantEvo = this.selectedLens.nominal;
        this.constantBarrett = this.selectedLens.nominal;
        this.constantHofferQ = this.selectedLens.hofferQPACD;
        this.constantHolladay = this.selectedLens.holladay1SF;
        this.constantSrkt = this.selectedLens.srkta;
        this.constantA0 = this.selectedLens.haigisA0;
        this.constantA1 = this.selectedLens.haigisA1;
        this.constantA2 = this.selectedLens.haigisA2;
        this.constantCooke = this.selectedLens.nominal;
        this.disabledCalcul();
    }

    updateConstantPearl() {
        if (this.typePearl === "Manufacturer constant") {
            this.constantPearl = this.selectedLens.nominal;
        } else if (this.typePearl === "Optimized constant") {
            this.constantPearl = this.selectedLens.srktaOptimized;
        } else {
            this.constantPearl = this.allAConstant;
        }
    }

    updateConstantKane() {
        if (this.typeKane === "Manufacturer constant") {
            this.constantKane = this.selectedLens.nominal;
        } else if (this.typeKane === "Optimized constant") {
            this.constantKane = this.selectedLens.srktaOptimized;
        } else {
            this.constantKane = this.allAConstant;
        }
    }

    updateConstantHofferQST() {
        if (this.typeHofferQst === "Manufacturer constant") {
            this.constantHofferQst = this.selectedLens.hofferQPACD;
        } else if (this.typeHofferQst === "Optimized constant") {
            this.constantHofferQst = this.selectedLens.hofferQPACDOptimized;
        } else {
            this.constantHofferQst = null;
        }
    }

    updateConstantEvo() {
        if (this.typeEvo === "Manufacturer constant") {
            this.constantEvo = this.selectedLens.nominal;
        } else if (this.typeEvo === "Optimized constant") {
            this.constantEvo = this.selectedLens.srktaOptimized;
        } else {
            this.constantEvo = this.allAConstant;
        }
    }

    updateConstantBarrett() {
        if (this.typeBarrett === "Manufacturer constant") {
            this.constantBarrett = this.selectedLens.nominal;
        } else if (this.typeBarrett === "Optimized constant") {
            this.constantBarrett = this.selectedLens.srktaOptimized;
        } else {
            this.constantBarrett = this.allAConstant;
        }
    }

    updateConstantHofferQ() {
        if (this.typeHofferQ === "Manufacturer constant") {
            this.constantHofferQ = this.selectedLens.hofferQPACD;
        } else if (this.typeHofferQ === "Optimized constant") {
            this.constantHofferQ = this.selectedLens.hofferQPACDOptimized;
        } else {
            this.constantHofferQ = null;
        }
    }

    updateConstantHolladay() {
        if (this.typeHolladay === "Manufacturer constant") {
            this.constantHolladay = this.selectedLens.holladay1SF;
        } else if (this.typeHolladay === "Optimized constant") {
            this.constantHolladay = this.selectedLens.holladay1SFOptimized;
        } else {
            this.constantHolladay = null;
        }
    }

    updateConstantSrkt() {
        if (this.typeSrkt === "Manufacturer constant") {
            this.constantSrkt = this.selectedLens.srkta;
        } else if (this.typeSrkt === "Optimized constant") {
            this.constantSrkt = this.selectedLens.srktaOptimized;
        } else {
            this.constantSrkt = this.allAConstant;
        }
    }

    updateConstantHaigis() {
        if (this.typeHaigis === "Manufacturer constant") {
            this.constantA0 = this.selectedLens.haigisA0;
            this.constantA1 = this.selectedLens.haigisA1;
            this.constantA2 = this.selectedLens.haigisA2;
        } else if (this.typeHaigis === "Optimized constant") {
            this.constantA0 = this.selectedLens.haigisA0Optimized;
            this.constantA1 = this.selectedLens.haigisA1Optimized;
            this.constantA2 = this.selectedLens.haigisA2Optimized;
        } else {
            this.constantA0 = null;
            this.constantA1 = null;
            this.constantA2 = null;
        }
    }

    updateConstantCooke() {
        if (this.typeCooke === "Manufacturer constant") {
            this.constantCooke = this.selectedLens.nominal;
        } else if (this.typeCooke === "Optimized constant") {
            this.constantCooke = this.selectedLens.cookeOptimized;
        } else {
            this.constantCooke = this.allAConstant;
        }
    }

    selectBiometry(id: number) {
        this.biometryService.getExamById(id, this.idOrga).subscribe({
            next: (biometry) => {
                this.selectedBiometry = biometry;
            }
        });
    }

    selectConstantType() {
        this.isConstants = true;
        this.typePearl = this.constantType;
        this.typeHofferQst = this.constantType;
        this.typeEvo = this.constantType;
        this.typeBarrett = this.constantType;
        this.typeKane = this.constantType;
        this.typeHofferQ = this.constantType;
        this.typeSrkt = this.constantType;
        this.typeHolladay = this.constantType;
        this.typeHaigis = this.constantType;
        this.typeCooke = this.constantType;
        this.updateAllConstants();
    }

    calculate(): Calcul {
        this.calcul.eyeSide = this.side as "OD" | "OS" | "OU";
        this.calcul.constantType = this.constantType;

        const availableFormulas = {
            Pearl: { constantType: this.typePearl, value: this.constantPearl?.toString() },
            Cooke: { constantType: this.typeCooke, value: this.constantCooke?.toString() },
            Kane: { constantType: this.typeKane, value: this.constantKane?.toString() },
            HofferQST: { constantType: this.typeHofferQst, value: this.constantHofferQst?.toString() },
            EVO: { constantType: this.typeEvo, value: this.constantEvo?.toString() },
            Barrett: { constantType: this.typeBarrett, value: this.constantBarrett?.toString() },
            HofferQ: { constantType: this.typeHofferQ, value: this.constantHofferQ?.toString() },
            Holladay: { constantType: this.typeHolladay, value: this.constantHolladay?.toString() },
            SRKT: { constantType: this.typeSrkt, value: this.constantSrkt?.toString() },
            Haigis: { constantType: this.typeHaigis, value: `a0=${this.constantA0};a1=${this.constantA1};a2=${this.constantA2}` },
        };

        this.calcul.exam = this.selectedBiometry;
        this.calcul.targetRefraction = this.targetRefraction;
        this.calcul.lens = this.selectedLens;

        this.calcul.constants = this.formulas.map((formula) => ({
            constantType: availableFormulas[formula].constantType,
            value: availableFormulas[formula].value,
            formula: formula,
        }));

        return this.calcul;
    }

    notifyParentOfChange(): void {
        this.disabledCalcul();
    }

    disabledCalcul() {
        this.desabledCalcul = this.targetRefraction == undefined || this.targetRefraction == null || !this.selectedManufacturer || !this.selectedLens || this.formulas.length == 0;
        this.disabledStatusChange.emit(this.desabledCalcul);
        this.cdr.detectChanges();
    }

    getCopy(calcul: Calcul) {
        this.allAConstant = null;
        this.targetRefraction = calcul.targetRefraction;
        if (calcul.lens) {
            this.selectedManufacturer = calcul.lens.lensManufacturer;
            this.importLenses();
            this.selectedLens = calcul.lens;
            this.updateConstants();
        }
        this.formulas = calcul.constants.map((cons) => cons.formula);
        this.constantType = calcul.constantType;
        this.copyConstants(calcul);
        this.notifyParentOfChange();
    }

    updateFormula() {
        if(this.calcul.isSecondEye) {
            this.formulas = ['Pearl'];
            this.disabledFormula = true;
        } else {
            this.formulas = ['Pearl', 'Cooke'];
            this.disabledFormula = false;
        }
    }

    copyConstants(calcul: Calcul) {
        calcul.constants.forEach((cal) => {
            switch (cal.formula) {
                case "Pearl":
                    this.typePearl = cal.constantType;
                    this.constantPearl = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "Cooke":
                    this.typeCooke = cal.constantType;
                    this.constantCooke = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "Kane":
                    this.typeKane = cal.constantType;
                    this.constantKane = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "HofferQST":
                    this.typeHofferQst = cal.constantType;
                    this.constantHofferQst = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "EVO":
                    this.typeEvo = cal.constantType;
                    this.constantEvo = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "Barrett":
                    this.typeBarrett = cal.constantType;
                    this.constantBarrett = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "HofferQ":
                    this.typeHofferQ = cal.constantType;
                    this.constantHofferQ = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "Holladay":
                    this.typeHolladay = cal.constantType;
                    this.constantHolladay = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "SRKT":
                    this.typeSrkt = cal.constantType;
                    this.constantSrkt = isNaN(parseFloat(cal.value)) ? undefined : parseFloat(cal.value);
                    break;
                case "Haigis":
                    this.typeHaigis = cal.constantType;
                    const values = cal.value.split(";");
                    this.constantA0 = isNaN(parseFloat(values[0]?.split("=")[1])) ? undefined : parseFloat(values[0]?.split("=")[1]);
                    this.constantA1 = isNaN(parseFloat(values[1]?.split("=")[1])) ? undefined : parseFloat(values[1]?.split("=")[1]);
                    this.constantA2 = isNaN(parseFloat(values[2]?.split("=")[1])) ? undefined : parseFloat(values[2]?.split("=")[1]);
                    break;
                default:
                    console.log("Unknown formula");
                    break;
            }
        });
    }
}
