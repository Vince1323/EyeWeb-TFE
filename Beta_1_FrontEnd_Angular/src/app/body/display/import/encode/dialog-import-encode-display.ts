import { Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { Organization } from "../../../api/Organization";
import { AutoCompleteCompleteEvent } from "primeng/autocomplete";
import { AuthService } from "../../../service/AuthService";
import { OrganizationService } from "../../../service/OrganizationService";
import { Message, MessageService } from 'primeng/api';
import {Patient} from "../../../api/patient/Patient";
import {PatientService} from "../../../service/PatientService";
import { Router } from '@angular/router';


@Component({
    templateUrl: './dialog-import-encode-display.html',
    selector: 'dialog-import-encode-display',
    encapsulation: ViewEncapsulation.None,
})
export class DialogImportEncodeDisplay implements OnInit {

    @Input() visible: boolean = false;
    @Output() visibleModified = new EventEmitter<boolean>();

    user = this.authService.getUserInfoFromToken();

    idOrgaSelected: number[];
    organizations: Organization[];
    filteredOrganizations!: Organization[];
    selectedOrganizations: Organization[];
    newPatient = {} as Patient;

    constructor(
        private authService: AuthService,
        private organizationService: OrganizationService,
        private messageService: MessageService,
        private patientService: PatientService,
        private router: Router,
    ) { }

    ngOnInit() {
        this.organizationService
            .getOrganizationsByUserId(this.user.id)
            .subscribe((result) => {
                this.organizations = result;
                this.selectedOrganizations = this.organizations;
                this.idOrgaSelected = this.selectedOrganizations.map(org => org.id);
            });
    }

    filterOrganization(event: AutoCompleteCompleteEvent) {
        let filtered: any[] = [];
        const query = event.query.toLowerCase();
        this.organizations.forEach(org => {
            if (org.name.toLowerCase().includes(query)) {
                filtered.push(org);
            }
        });
        this.filteredOrganizations = filtered;
        this.idOrgaSelected = this.selectedOrganizations.map(org => org.id);
    }

    onDialogClose(): void {
        this.visibleModified.emit(false);
    }

    savePatient(newPatient: Patient){
        this.patientService.addPatient(newPatient, this.idOrgaSelected).subscribe({
            next: (savedPatient) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: 'Patient added successfully',
                });
                this.onDialogClose();
                this.router.navigate([`/cataract/patients/${savedPatient.id}/biometrics`]);
            },
            error: (err) => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Failed to add patient',
                });
            },
        });
    }

}
