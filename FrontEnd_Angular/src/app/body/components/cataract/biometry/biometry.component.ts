import { Component, OnInit } from "@angular/core";
import { LayoutService } from "../../../../layout/service/app.layout.service";
import { AuthService } from "../../../service/AuthService";
import { StorageService } from "../../../service/StorageService";
import { MenuItem, MessageService, ConfirmationService } from "primeng/api";
import { DeviceDetectionService } from "../../../service/DeviceDetectionService";
import { FormBuilder, FormGroup } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { Biometry } from "../../../api/exam/Biometry";
import { Patient } from "../../../api/patient/Patient";
import { CataractService } from "../../../service/CataractService";
import { ExamSummary } from "../../../api/exam/ExamSummary";
import { Exam } from "../../../api/exam/Exam";
import { MachineService } from "../../../service/MachineService";

@Component({
    templateUrl: "./biometry.component.html",
    providers: [MessageService, ConfirmationService, DatePipe, MachineService, CataractService, DeviceDetectionService],
})
export class BiometryComponent implements OnInit {
    user = this.authService.getUserInfoFromToken();
    patient = this.storageService.get("patient");
    idOrga: number[] = this.storageService.get("organization");

    //VAR RESPONSIVE
    isMobileDevice: boolean = false;

    //VAR ITEMS
    itemsOS: MenuItem[] | undefined;
    itemsOD: MenuItem[] | undefined;
    modalEditOD: boolean = false;
    modalEditOS: boolean = false;
    modalAverageOD: boolean = false;
    modalAverageOS: boolean = false;
    biometryForm: FormGroup;
    isFormsValid: boolean = false;
    modalAddBio: boolean = false;
    newBiometries: Exam[] = null;

    biometriesOS: ExamSummary[] = [];
    biometriesOD: ExamSummary[] = [];
    selectedBiometryOS = {} as Exam;
    selectedBiometryOD = {} as Exam;
    lightOD!: ExamSummary;
    lightOS!: ExamSummary;
    
    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private cataractService: CataractService,
        private storageService: StorageService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private deviceService: DeviceDetectionService,
        private fb: FormBuilder,
        private datePipe: DatePipe
    ) {}

    confirmDelete(event: Event, side: string) {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: "Do you want to delete this record?",
            header: "Delete Confirmation",
            icon: "pi pi-info-circle",
            acceptButtonStyleClass: "p-button-danger p-button-text",
            rejectButtonStyleClass: "p-button-text p-button-text",
            acceptIcon: "none",
            rejectIcon: "none",

            accept: () => {
                this.deleteBiometry(side);
            },
            reject: () => {
                this.messageService.add({
                    severity: "error",
                    summary: "Deletion Rejected",
                    detail: "Deletion of biometric data has been cancelled.",
                });
            },
        });
    }

    ngOnInit() {
        this.itemsOD = [
            {
                label: "Edit",
                icon: "pi pi-fw pi-pencil",
                command: () => {
                    this.initializeForm();
                    this.checkFormValidity();
                    this.modalEditOD = true;
                },
            },
            {
                label: "Average",
                icon: "pi pi-fw pi pi-calculator",
                command: () => {
                    this.modalAverageOD = true;
                },
            },
            {
                label: "Delete",
                icon: "pi pi-fw pi-trash",
                command: () => {
                    this.confirmDelete(event, "OD");
                },
            },
        ];
        this.itemsOS = [
            {
                label: "Edit",
                icon: "pi pi-fw pi-pencil",
                command: () => {
                    this.initializeForm();
                    this.checkFormValidity();
                    this.modalEditOS = true;
                },
            },
            {
                label: "Average",
                icon: "pi pi-fw pi pi-calculator",
                command: () => {
                    this.modalAverageOS = true;
                },
            },
            {
                label: "Delete",
                icon: "pi pi-fw pi-trash",
                command: () => {
                    this.confirmDelete(event, "OS");
                },
            },
        ];
        this.isMobileDevice = this.deviceService.isMobile();
        this.cataractService.getAllExamsListByPatientId(this.patient.id, this.idOrga, "OS").subscribe({
            next: (biometries) => {
                const formattedBiometries = biometries.map((biometry) => ({
                    ...biometry,
                    calculDate: this.datePipe.transform(biometry.calculDate, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.biometriesOS = formattedBiometries;
                if (formattedBiometries.length > 0) {
                    this.lightOS = formattedBiometries[0];
                    this.selectBiometry(formattedBiometries[0].id, "OS");
                }
            },
        });

        this.cataractService.getAllExamsListByPatientId(this.patient.id, this.idOrga, "OD").subscribe({
            next: (biometries) => {
                const formattedBiometries = biometries.map((biometry) => ({
                    ...biometry,
                    calculDate: this.datePipe.transform(biometry.calculDate, "yyyy-MM-dd HH:mm:ss"),
                }));
                this.biometriesOD = formattedBiometries;
                if (formattedBiometries.length > 0) {
                    this.lightOD = formattedBiometries[0];
                    this.selectBiometry(formattedBiometries[0].id, "OD");
                }
            },
        });
    }

    selectBiometry(id: number, side: "OD" | "OS") {
        this.cataractService.getExamById(id, this.idOrga).subscribe({
            next: (biometry) => {
                biometry.selected = true;

                if (side === "OD") {
                    this.selectedBiometryOD = biometry;
                } else if (side === "OS") {
                    this.selectedBiometryOS = biometry;
                }
            },
        });
    }

    updateSelectedBiometry(selectedBiometry: Exam, side: "OD" | "OS") {
        let edit = {} as Exam;
        edit.id = selectedBiometry.id;
        edit.selected = selectedBiometry.selected;
        this.cataractService.editSelectExam(edit, this.idOrga).subscribe({
            next: (biometry) => {
                if (side === "OD") {
                    this.selectedBiometryOD = biometry;
                } else if (side === "OS") {
                    this.selectedBiometryOS = biometry;
                }
            },
        });
    }

    initializeForm(): void {
        this.biometryForm = this.fb.group({
            selectedBiometryOD: this.fb.group({
                id: [this.selectedBiometryOD.id],
                k1: [this.selectedBiometryOD.k1],
                k1Axis: [this.selectedBiometryOD.k1Axis],
                k2: [this.selectedBiometryOD.k2],
                k2Axis: [this.selectedBiometryOD.k2Axis],
                al: [this.selectedBiometryOD.al],
                acd: [this.selectedBiometryOD.acd],
                kAvg: [this.selectedBiometryOD.kAvg],
                kAstig: [this.selectedBiometryOD.kAstig],
                wtw: [this.selectedBiometryOD.wtw],
                pupilDia: [this.selectedBiometryOD.pupilDia],
                cord: [this.selectedBiometryOD.cord],
                snr: [this.selectedBiometryOD.snr],
                z40: [this.selectedBiometryOD.z40],
                cct: [this.selectedBiometryOD.cct],
                lensThickness: [this.selectedBiometryOD.lensThickness],
                machine: [this.selectedBiometryOD.machine],
            }),
            selectedBiometryOS: this.fb.group({
                id: [this.selectedBiometryOS.id],
                k1: [this.selectedBiometryOS.k1],
                k1Axis: [this.selectedBiometryOS.k1Axis],
                k2: [this.selectedBiometryOS.k2],
                k2Axis: [this.selectedBiometryOS.k2Axis],
                al: [this.selectedBiometryOS.al],
                acd: [this.selectedBiometryOS.acd],
                kAvg: [this.selectedBiometryOS.kAvg],
                kAstig: [this.selectedBiometryOS.kAstig],
                wtw: [this.selectedBiometryOS.wtw],
                pupilDia: [this.selectedBiometryOS.pupilDia],
                cord: [this.selectedBiometryOS.cord],
                snr: [this.selectedBiometryOS.snr],
                z40: [this.selectedBiometryOS.z40],
                cct: [this.selectedBiometryOS.cct],
                lensThickness: [this.selectedBiometryOS.lensThickness],
                machine: [this.selectedBiometryOS.machine],
            }),
        });
    }

    get selectedBiometryODCast(): FormGroup {
        return this.biometryForm.get("selectedBiometryOD") as FormGroup;
    }

    get selectedBiometryOSCast(): FormGroup {
        return this.biometryForm.get("selectedBiometryOS") as FormGroup;
    }

    checkFormValidity() {
        const validityOD = this.checkValidity(this.biometryForm.get("selectedBiometryOD") as FormGroup);
        const validityOS = this.checkValidity(this.biometryForm.get("selectedBiometryOS") as FormGroup);
        // Cas où les deux formulaires sont entièrement vides
        if (validityOD.isEmpty && validityOS.isEmpty) {
            this.isFormsValid = false;
            return;
        }
        // Cas où au moins un formulaire est partiellement rempli mais pas totalement valide
        if ((validityOD.isPartiallyFilled && !validityOD.isFullyValid) || (validityOS.isPartiallyFilled && !validityOS.isFullyValid)) {
            this.isFormsValid = false;
            return;
        }
        this.isFormsValid = true;
    }

    checkValidity(form: FormGroup): {
        isEmpty: boolean;
        isFullyValid: boolean;
        isPartiallyFilled: boolean;
    } {
        const values = form.value;
        const requiredFields = ["k1", "k2", "al", "acd"];
        const allFieldsEmpty = Object.keys(values).every((key) => !values[key]);
        const isPartiallyFilled = Object.keys(values).some((key) => !!values[key]);
        const requiredFieldsFilled = requiredFields.every((field) => !!values[field]);

        return {
            isEmpty: allFieldsEmpty,
            isFullyValid: requiredFieldsFilled,
            isPartiallyFilled: isPartiallyFilled && !allFieldsEmpty,
        };
    }

    deleteBiometry(side: string) {
        if (side == "OD") {
            this.cataractService.deleteExam(this.selectedBiometryOD.id, this.idOrga).subscribe({
                next: (biometry) => {
                    this.messageService.add({
                        severity: "success",
                        summary: "Biometry Deleted",
                        detail: "Biometric data has been successfully deleted.",
                    });
                    window.location.reload();
                },
            });
        } else if (side == "OS") {
            this.cataractService.deleteExam(this.selectedBiometryOS.id, this.idOrga).subscribe({
                next: (biometry) => {
                    this.messageService.add({
                        severity: "success",
                        summary: "Biometry Deleted",
                        detail: "Biometric data has been successfully deleted.",
                    });
                    window.location.reload();
                },
            });
        }
    }

    saveBiometry(side: string) {
        if (side == "OD") {
            let selected = this.selectedBiometryOD.selected;
            this.selectedBiometryOD = this.biometryForm.get("selectedBiometryOD").value;
            this.selectedBiometryOD.selected = selected;
            this.cataractService.editExam(this.selectedBiometryOD, this.idOrga).subscribe({
                next: (biometry) => {
                    this.selectedBiometryOD = biometry;
                    this.modalEditOD = false;
                    this.messageService.add({
                        severity: "success",
                        summary: "Biometry Modified",
                        detail: "Biometric data has been successfully updated.",
                    });
                },
            });
        } else if (side == "OS") {
            let selected = this.selectedBiometryOS.selected;
            this.selectedBiometryOS = this.biometryForm.get("selectedBiometryOS").value;
            this.selectedBiometryOS.selected = selected;
            this.cataractService.editExam(this.selectedBiometryOS, this.idOrga).subscribe({
                next: (biometry) => {
                    this.selectedBiometryOS = biometry;
                    this.modalEditOS = false;
                    this.messageService.add({
                        severity: "success",
                        summary: "Biometry Modified",
                        detail: "Biometric data has been successfully updated.",
                    });
                },
            });
        }
    }

    hideAverageOS(visible: boolean) {
        this.modalEditOS = visible;
        location.reload();
    }

    hideAverageOD(visible: boolean) {
        this.modalEditOD = visible;
        location.reload();
    }

    addNewBiometry() {
        this.modalAddBio = true;
    }

    onBiometryModified(modifiedBiometries: Biometry[]) {
        modifiedBiometries = modifiedBiometries.filter((item) => {
            const firstKey = Object.keys(item)[0];
            return item[firstKey] !== null;
        });
        this.onSaveBiometries(modifiedBiometries, this.patient.id);
    }

    onSaveBiometries(modifiedBiometries: Biometry[], patientId: number) {        
        this.cataractService
            .addExams(modifiedBiometries, patientId, this.idOrga)
            .subscribe({
                next: () => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Success',
                        detail: 'Biometries are saved.',
                    });
                    this.modalAddBio = false;
                    location.reload();
                }
            });
    }

    private formatBirthDate(dateString: string): string {
        const date = new Date(dateString);
        const day = ("0" + date.getDate()).slice(-2);
        const month = ("0" + (date.getMonth() + 1)).slice(-2);
        const year = date.getFullYear();
        return `${year}-${month}-${day}`;
    }
}
