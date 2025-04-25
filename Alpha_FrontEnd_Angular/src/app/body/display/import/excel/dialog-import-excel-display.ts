import { Component,  EventEmitter,  Input, OnInit, Output, ViewChild, ViewEncapsulation } from '@angular/core';
import { Organization } from "../../../api/Organization";
import { AutoCompleteCompleteEvent } from "primeng/autocomplete";
import { AuthService } from "../../../service/AuthService";
import { OrganizationService } from "../../../service/OrganizationService";
import { MachineService } from "../../../service/MachineService";
import { CataractService } from "../../../service/CataractService";
import { MenuItem } from "primeng/api";
import { Message, MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { EventSourceService } from 'src/app/body/service/EventSourceService';
import { ScrollPanel } from 'primeng/scrollpanel';

@Component({
    templateUrl: './dialog-import-excel-display.html',
    selector: 'dialog-import-excel-display',
    styleUrl: './dialog-import-excel-display.scss',
    encapsulation: ViewEncapsulation.None,
})
export class DialogImportExcelDisplay implements OnInit {

    @Input() visible: boolean = false;
    @Output() visibleModified = new EventEmitter<boolean>();
    @ViewChild('scrollPanelRef') scrollPanel!: ScrollPanel;

    steps: MenuItem[] | undefined;
    activeStepIndex: number = 0;
    isUploading: boolean = false;
    logs: string[] = [];

    user = this.authService.getUserInfoFromToken();

    idOrgaSelected: number[];
    organizations: Organization[];
    filteredOrganizations!: Organization[];
    selectedOrganizations: Organization[];

    biometers!: any[];
    selectedBiometer: any;
    filteredBiometers: any[];

    typeImport = 'text/csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
    uploadedFiles: any[] = [];
    private sseSubscription!: Subscription;

    constructor(
        private authService: AuthService,
        private organizationService: OrganizationService,
        private biometerService: MachineService,
        private cataractService: CataractService,
        private messageService: MessageService,
        private eventSourceService: EventSourceService
    ) { }

    ngOnInit() {
        this.steps = [
            { label: 'Selection' },
            { label: 'Uploading' },
        ];

        this.organizationService
            .getOrganizationsByUserId(this.user.id)
            .subscribe((result) => {
                this.organizations = result;
                this.selectedOrganizations = this.organizations;
            });

        this.biometerService.getBiometers().subscribe({
            next: (res) => {
                this.biometers = res.biometers;
            }
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

    filterBiometer(event: AutoCompleteCompleteEvent) {
        const query = event.query.toLowerCase();
        this.filteredBiometers = this.biometers.filter(bio =>
            bio.name.toLowerCase().includes(query)
        );
    }

    onUpload(event: any) {
        this.isUploading = true;
        this.activeStepIndex = 1;
        const file = event.files[0];
        const fileExtension = file.name.split('.').pop().toLowerCase();
        this.idOrgaSelected = this.selectedOrganizations.map(org => org.id);

        if (file && ['csv', 'xlsx'].includes(fileExtension)) {
            this.insertFile(file, this.selectedBiometer.name, this.idOrgaSelected);
            this.listenToLogs();
        }
    }

    insertFile(file: any, selectedBiometerName: string, idOrga: number[]) {
        this.cataractService.insertFile(file, selectedBiometerName, idOrga).subscribe({
            next: (resp) => {
                this.isUploading = false;
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Biometries imported!' });
                //Fermer le modal
            },
            error: () => {
                this.isUploading = false;
                //Fermer le modal
            }
        });
    }

    listenToLogs() {
        const url = '/cataract/import/logs';
        const options = {
            headers: { Authorization: `${this.authService.getToken()}` },
            withCredentials: true
        };

        // Connexion au service SSE
        this.sseSubscription = this.eventSourceService
            .connectToServerSentEvents(url, options)
            .subscribe({
                next: (event: MessageEvent) => {
                    const message = (event as MessageEvent).data;
                    if (message != null && message != undefined) {
                        this.logs.push(message);
                        this.scrollToBottom();
                    }
                }
            });
    }

    scrollToBottom() {
        setTimeout(() => {
            const scrollHeight = this.scrollPanel.contentViewChild.nativeElement.scrollHeight;
            this.scrollPanel.scrollTop(scrollHeight);
        });
    }

    onDialogClose(): void {
        this.activeStepIndex = 0;
        this.logs = [];
        this.visibleModified.emit(false);
        // Nettoie la connexion SSE
        if(this.sseSubscription) {
            this.sseSubscription.unsubscribe();
            this.eventSourceService.close();    
        }
    }

}
