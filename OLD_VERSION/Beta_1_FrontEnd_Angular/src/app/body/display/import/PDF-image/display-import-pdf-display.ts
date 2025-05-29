import { Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation } from "@angular/core";
import { Organization } from "../../../api/Organization";
import { AutoCompleteCompleteEvent } from "primeng/autocomplete";
import { AuthService } from "../../../service/AuthService";
import { OrganizationService } from "../../../service/OrganizationService";
import { MenuItem } from "primeng/api";
import { Message, MessageService } from "primeng/api";
import { Subscription } from "rxjs";
import { OcrService } from "src/app/body/service/OcrService";
import { Patient } from "src/app/body/api/patient/Patient";
import { MachineService } from "src/app/body/service/MachineService";
import { Exam } from "src/app/body/api/exam/Exam";
import { PatientService } from "src/app/body/service/PatientService";
import { CataractService } from "src/app/body/service/CataractService";

@Component({
    templateUrl: "./dialog-import-pdf-display.html",
    selector: "dialog-import-pdf-display",
    encapsulation: ViewEncapsulation.None,
})
export class DialogImportPDFDisplay implements OnInit {
    @Input() visible: boolean = false;
    @Output() visibleModified = new EventEmitter<boolean>();

    steps: MenuItem[] | undefined;
    activeStepIndex: number = 0;
    isUploading: boolean = false;
    selectedSide: string = "OU";
    sideOptions: any[] = [
        { name: "OU", value: "OU" },
        { name: "OD", value: "OD" },
        { name: "OS", value: "OS" },
    ];

    user = this.authService.getUserInfoFromToken();

    idOrgaSelected: number[];
    organizations: Organization[];
    filteredOrganizations!: Organization[];
    selectedOrganizations: Organization[];
    newPatient = {} as Patient;
    newBiometries: Exam[];

    biometers!: any[];
    selectedBiometer: any;
    filteredBiometers: any[];

    typeImport = "image/png, image/jpeg, application/pdf";
    uploadedFiles: any[] = [];
    private sseSubscription!: Subscription;

    constructor(
        private authService: AuthService,
        private organizationService: OrganizationService,
        private biometerService: MachineService,
        private ocrService: OcrService,
        private messageService: MessageService,
        private patientService: PatientService,
        private cataractService: CataractService
    ) {}

    ngOnInit() {
        this.steps = [{ label: "Selection" }, { label: "Uploading" }, { label: "Patient" }, { label: "Biometry" }, { label: "Confirmation" }];

        this.organizationService.getOrganizationsByUserId(this.user.id).subscribe((result) => {
            this.organizations = result;
            this.selectedOrganizations = this.organizations;
        });

        this.biometerService.getBiometers().subscribe({
            next: (res) => {
                this.biometers = res.biometers;
            },
        });
    }

    filterOrganization(event: AutoCompleteCompleteEvent) {
        let filtered: any[] = [];
        const query = event.query.toLowerCase();
        this.organizations.forEach((org) => {
            if (org.name.toLowerCase().includes(query)) {
                filtered.push(org);
            }
        });
        this.filteredOrganizations = filtered;
        this.idOrgaSelected = this.selectedOrganizations.map((org) => org.id);
    }

    filterBiometer(event: AutoCompleteCompleteEvent) {
        const query = event.query.toLowerCase();
        this.filteredBiometers = this.biometers.filter((bio) => bio.name.toLowerCase().includes(query));
    }

    selectSide(): void {
        if (!this.selectedSide) {
            this.selectedSide = "OU";
        }
    }

    onPatientModified(patient: Patient) {
        this.newPatient = patient;
        this.activeStepIndex = 3;
    }

    onBiometryModified(modifiedBiometries: Exam[]) {
        modifiedBiometries = modifiedBiometries.filter((item) => {
            const firstKey = Object.keys(item)[0];
            return item[firstKey] !== null;
        });
        this.newBiometries = modifiedBiometries;
        this.activeStepIndex = 4;
    }

    onSavePatientBiometriesModified() {
        this.patientService.addPatient(this.newPatient, this.idOrgaSelected).subscribe({
            next: (savedPatient) => {
                this.cataractService
                .addExams(this.newBiometries, savedPatient.id, this.idOrgaSelected)
                .subscribe({
                    next: () => {
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Success',
                            detail: 'Patient and biometries added successfully',
                        });
                        location.reload();
                    }
                });
                this.onDialogClose();
            },
            error: (error) => {
                this.activeStepIndex = 5;
            }
        });
    }

    goBackPatient(event: Event) {
        this.activeStepIndex = 2;
    }

    goBackBiometry(event: Event) {
        this.activeStepIndex = 3;
    }

    onUpload(event: any) {
        this.isUploading = true;
        this.activeStepIndex = 1;
        const file = event.files[0];
        const fileExtension = file.name.split(".").pop().toLowerCase();
        this.idOrgaSelected = this.selectedOrganizations.map((org) => org.id);

        if (file && ["png", "jpg", "jpeg"].includes(fileExtension)) {
            this.convertImage(event, fileExtension);
        } else {
            this.ocrReconizeText(file, this.selectedBiometer.name, this.idOrgaSelected);
        }
    }

    ocrReconizeText(file: any, selectedBiometerName: string, idOrga: number[]) {
        this.ocrService.recognizeText(file, selectedBiometerName, this.selectedSide, idOrga).subscribe({
            next: (resp) => {
                this.newPatient = resp;
                this.newBiometries = resp.examDtos;
                this.activeStepIndex = 2;
                //TODO: Enlever les deux lignes suivantes:
                //console.log("Résultat du texte détecté par l'OCR: \n" + resp.hobbies);
                resp.hobbies = null;
                console.log(resp);
            },
            error: (error) => {
                this.activeStepIndex = 5;
            },
        });
    }

    async convertImage(event: any, fileType: string) {
        try {
            let pdfFile = event;
            if (fileType === "jpeg" || fileType === "jpg" || fileType === "png") {
                pdfFile = await this.ocrService.convertImageToPdf(event.files[0], event.files[0].name);
            }
            this.ocrReconizeText(pdfFile, this.selectedBiometer.name, this.idOrgaSelected);
        } catch (error) {
            console.error("Error during file conversion", error);
            alert("Failed to convert image and send PDF.");
        }
    }

    onDialogClose(): void {
        this.activeStepIndex = 0;
        this.visibleModified.emit(false);
    }
}
