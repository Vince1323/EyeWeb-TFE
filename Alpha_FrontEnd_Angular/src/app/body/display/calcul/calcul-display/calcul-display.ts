import { Component, Input, OnInit } from "@angular/core";
import { AuthService } from "../../../service/AuthService";
import { DeviceDetectionService } from "../../../service/DeviceDetectionService";
import { StorageService } from "../../../service/StorageService";
import { CalculService } from "../../../service/CalculService";
import { DatePipe } from "@angular/common";
import { CataractService } from "../../../service/CataractService";
import { CalculMatrix } from "src/app/body/api/calcul/CalculMatrix";

@Component({
    templateUrl: "./calcul-display.html",
    selector: "calcul-display",
    providers: [AuthService, CataractService, StorageService, DatePipe, DeviceDetectionService],
})
export class CalculDisplay implements OnInit {
    patient = this.storageService.get("patient");
    idOrga: number[] = this.storageService.get("organization");

    @Input() calculs: any[] = [];
    @Input() selectedCalcul: CalculMatrix;
    @Input() light!: any;
    @Input() side: string;

    selectedCalculFiltered: CalculMatrix;
    selectedPower: number[] | null = null;

    //VAR RESPONSIVE
    isMobileDevice: boolean = false;

    //VAR NEW CALCUL
    target: string;

    constructor(
        private storageService: StorageService,
        private calculService: CalculService,
        private deviceService: DeviceDetectionService,
    ) {}

    ngOnInit() {
        this.isMobileDevice = this.deviceService.isMobile();
        if(this.light) {
            this.selectCalcul(this.light.id);
        }
    }

    selectCalcul(id: number) {
        this.calculService.getCalculById(id, this.idOrga).subscribe({
            next: (calcul) => {
                this.selectedCalcul = calcul;
                this.selectedCalcul.valueMatrixConversion = calcul.valueMatrix.map((row, index) => ({
                    id: row[0],
                    values: row
                  }));
                this.selectedCalculFiltered = this.filterCalculMatrix(this.selectedCalcul);
                this.selectedCalcul.filter = false;
                this.target = "Target: " + this.selectedCalcul.targetRefraction;
                this.autoSelectPower();
            }
        });
    }

    autoSelectPower() {
        if (this.selectedCalculFiltered?.valueMatrixConversion) {
          this.selectedPower = this.selectedCalculFiltered.valueMatrixConversion.find(row => row.id === this.selectedCalcul.selectedPower) || null;
        }
    }

    filterCalculMatrix(calcul: CalculMatrix): CalculMatrix {
        return {
          ...calcul,
          valueMatrixConversion: calcul.valueMatrixConversion
          ? calcul.valueMatrixConversion.filter(row => 
            !row.id.toString().includes('.25') && 
            !row.id.toString().includes('.75')
          )
        : null
        };
    }

    removeFilter() {
        if(this.selectedCalcul.filter) {
            this.selectedCalculFiltered = this.selectedCalcul;
        } else {
            this.selectedCalculFiltered = this.filterCalculMatrix(this.selectedCalculFiltered);
        }
    }

    selectPower(selectedPower: any) {
        this.calculService.selectPower(this.selectedCalcul.id, selectedPower.id, this.idOrga).subscribe();
    }

    unSelectPower() {
        this.calculService.selectPower(this.selectedCalcul.id, 0, this.idOrga).subscribe();
    }
}