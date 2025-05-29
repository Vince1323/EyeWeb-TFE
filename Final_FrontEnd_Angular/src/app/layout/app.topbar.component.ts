import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { LayoutService, ColorScheme } from 'src/app/layout/service/app.layout.service';
import { AuthService } from 'src/app/body/service/AuthService';
import { ConfirmationService } from 'primeng/api';
import { MessageService } from 'primeng/api';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html',
    providers: [ConfirmationService, MessageService],
})
export class AppTopbarComponent implements OnInit {
    @ViewChild('menubutton') menuButton!: ElementRef;

    // Gestion du thème clair/sombre
    darkTheme: boolean = this.layoutService.config().colorScheme === 'dim';

    // Variable contenant les infos patient et utilisateur
    patientInfo: string = '';
    userName: string = '';
    userRole: string = '';

    constructor(
        public layoutService: LayoutService,
        public authService: AuthService,
        private confirmationService: ConfirmationService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        // Mise à jour des infos patient affichées
        document.addEventListener('patientLoaded', (event: any) => {
            const detail = event.detail;
            this.patientInfo = ` Patient : ${detail.name}  |  Birthdate :  ${detail.birthDate}`;
        });

        // Récupération des infos utilisateur
        if (this.authService.isAuthenticate()) {
            const userInfo = this.authService.getUserInfoFromToken();
            this.userName = `${userInfo.firstname} ${userInfo.lastname}`;
            this.userRole = userInfo.role;
        }
    }

    onMenuButtonClick() {
        this.layoutService.onMenuToggle();
    }

    onProfileButtonClick() {
        this.layoutService.showProfileSidebar();
    }

    changeTheme() {
        const newColorScheme: ColorScheme = this.darkTheme ? 'light' : 'dim';
        this.layoutService.config.update((config) => ({
            ...config,
            colorScheme: newColorScheme,
        }));
        this.darkTheme = !this.darkTheme;
    }

    logout() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to log out?',
            header: 'Logout Confirmation',
            icon: 'pi pi-exclamation-triangle',
            acceptLabel: 'Yes',
            rejectLabel: 'No',
            acceptButtonStyleClass: 'p-button-success',
            rejectButtonStyleClass: 'p-button-danger',
            accept: () => {
                this.authService.logout();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Logout',
                    detail: 'You have been successfully logged out.',
                });
            },
        });
    }

}
