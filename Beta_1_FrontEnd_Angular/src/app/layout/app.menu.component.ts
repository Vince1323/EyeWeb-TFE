import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { SelectionService } from '../body/service/SelectionService';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
    model: any[] = [];
    private patientSubscription!: Subscription;
    isPatientSelected: boolean = false;

    constructor(
        public layoutService: LayoutService,
        private selectionService: SelectionService,
    ) {}

    ngOnInit() {
        this.patientSubscription = this.selectionService
            .getPatientChanges()
            .subscribe((selectedPatient) => {
                this.isPatientSelected = !!selectedPatient;
                this.updateMenuItems();
            });
    }

    ngOnDestroy() {
        if (this.patientSubscription) {
            this.patientSubscription.unsubscribe();
        }
    }

    updateMenuItems() {
        let patient = this.selectionService.getPatient();

        this.model = [
            {
                label: 'Home',
                items: [
                    {
                        label: 'Home',
                        icon: 'pi pi-fw pi-home',
                        routerLink: [''],
                    },
                ],
            },
            {
                label: 'Quick Actions',
                items: [
                    {
                        label: 'Consultation',
                        icon: 'pi pi-lock',
                        disabled: true,
                    },
                    {
                        label: 'Exam',
                        icon: 'pi pi-lock',
                        disabled: true,
                    },
                ],
            },
            {
                label: 'Administrative',
                items: [
                    {
                        label: 'Patient informations',
                        icon: this.isPatientSelected
                            ? 'pi pi-info-circle'
                            : 'pi pi-lock',
                        routerLink: this.isPatientSelected
                            ? [`patients/${patient.id}/administrative`]
                            : null,
                        disabled: !this.isPatientSelected,
                    },
                    {
                        label: 'Medical background',
                        icon: this.isPatientSelected
                            ? 'pi pi-briefcase'
                            : 'pi pi-lock',
                        routerLink: this.isPatientSelected
                            ? [`patients/${patient.id}/background`]
                            : null,
                        disabled: !this.isPatientSelected,
                    },
                ],
            },
            {
                label: 'Workflow',
                items: [
                    {
                        label: 'Cataract',
                        icon: 'pi pi-fw pi-bookmark',
                        items: [
                            {
                                label: 'Summary',
                                icon: this.isPatientSelected ? 'pi pi-book' : 'pi pi-lock',
                                routerLink: this.isPatientSelected
                                    ? [`patients/${patient.id}/summary`]
                                    : null,
                                disabled: !this.isPatientSelected,
                            },
                            
                            {
                                label: 'Biometry',
                                icon: this.isPatientSelected
                                    ? 'pi pi-folder'
                                    : 'pi pi-lock',
                                routerLink: this.isPatientSelected
                                    ? [`cataract/patients/${patient.id}/biometrics`]
                                    : null,
                                disabled: !this.isPatientSelected,
                            },
                            {
                                label: 'Preoperative',
                                icon: this.isPatientSelected
                                    ? 'pi pi-search'
                                    : 'pi pi-lock',
                                routerLink: this.isPatientSelected
                                    ? [`cataract/patients/${patient.id}/preoperative`]
                                    : null,
                                disabled: !this.isPatientSelected,
                            },
                            {
                                label: 'IOL Calculator',
                                icon: this.isPatientSelected
                                    ? 'pi pi-calculator'
                                    : 'pi pi-lock',
                                routerLink: this.isPatientSelected
                                    ? [`cataract/patients/${patient.id}/calculators`]
                                    : null,
                                disabled: !this.isPatientSelected,
                            },
                            {
                                label: 'Surgery Planning',
                                icon: 'pi pi-lock',
                                disabled: true,
                            },
                            {
                                label: 'Surgery Protocol',
                                icon: 'pi pi-lock',
                                disabled: true,
                            },
                            {
                                label: 'Postoperative',
                                icon: 'pi pi-lock',
                                disabled: true,
                            },
                        ],
                    },
                    {
                        label: 'Refractive',
                        icon: 'pi pi-lock',
                        disabled: true,
                    },
                    {
                        label: 'Glaucoma',
                        icon: 'pi pi-lock',
                        disabled: true,
                    },
                ],
            },
            {
                label: 'Informations',
                items: [
                    {
                        label: 'Contact',
                        icon: 'pi pi-comments',
                        routerLink: ['/contact'],
                    },
                    {
                        label: 'FAQ',
                        icon: 'pi pi-question',
                        routerLink: ['/faq'],
                    },
                    {
                        label: 'Release Notes',
                        icon: 'pi pi-list',
                    },
                ],
            },
        ];
    }
}
