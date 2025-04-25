import { Component, EventEmitter, Input, OnInit, Output, AfterViewInit, ViewChild, ElementRef } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { AutoCompleteCompleteEvent } from "primeng/autocomplete";
import { Biometry } from "../../../api/exam/Biometry";
import { MachineService } from "../../../service/MachineService";

@Component({
    templateUrl: "./biometry-form.html",
    selector: "biometry-form",
    providers: [],
})
export class BiometryForm implements OnInit, AfterViewInit  {
    constructor(private machineService: MachineService) {}

    @Input() biometry!: Biometry;
    @Input() formGroup!: FormGroup;
    @Output() changeDetected = new EventEmitter<void>();

    machines!: any[];
    filteredBiometers!: any[];

    @ViewChild('kAvgRef', { read: ElementRef }) kAvgRef!: ElementRef;
    @ViewChild('astigRef', { read: ElementRef }) astigRef!: ElementRef;

    ngOnInit(): void {
        this.getBiometer();
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            const kAvgInput = this.kAvgRef?.nativeElement?.querySelector('input');
            const astigInput = this.astigRef?.nativeElement?.querySelector('input');

            if (kAvgInput) {
                kAvgInput.setAttribute('tabindex', '-1');
                kAvgInput.setAttribute('readonly', 'true');
                kAvgInput.setAttribute('disabled', 'true');
            }

            if (astigInput) {
                astigInput.setAttribute('tabindex', '-1');
                astigInput.setAttribute('readonly', 'true');
                astigInput.setAttribute('disabled', 'true');
            }
        });
    }

    notifyParentOfChange(): void {
        this.updateFields();
        this.changeDetected.emit();
    }

    getBiometer() {
        this.machineService.getBiometers().subscribe({
            next: (res) => {
                this.machines = res.biometers.map((biometer: { name: any }) => biometer.name);
            },
        });
    }

    filterBiometer(event: AutoCompleteCompleteEvent) {
        const query = event.query.toLowerCase();
        this.filteredBiometers = this.machines.filter((biometer) => biometer.toLowerCase().includes(query));
    }

    updateFields() {
        if (this.formGroup.get("k1")?.value && this.formGroup.get("k2")?.value) {
            const k1 = this.formGroup.get("k1")?.value;
            const k2 = this.formGroup.get("k2")?.value;

            this.formGroup.get("kAvg")?.setValue(((k1 + k2) / 2).toFixed(2));

            this.formGroup.get("kAstig")?.setValue((k2 - k1).toFixed(2));
        }

        if (this.formGroup.get("k1Axis")?.value) {
            const k1Axis = this.formGroup.get("k1Axis")?.value;

            if (k1Axis < 90) {
                this.formGroup.get("k2Axis")?.setValue((k1Axis + 90).toFixed(2));
            } else {
                this.formGroup.get("k2Axis")?.setValue((k1Axis - 90).toFixed(2));
            }
        }
    }
}
