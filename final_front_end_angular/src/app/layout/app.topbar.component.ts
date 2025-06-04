import { Component, ElementRef, ViewChild, OnInit, OnDestroy } from '@angular/core';
import { LayoutService, ColorScheme } from 'src/app/layout/service/app.layout.service';
import { AuthService } from 'src/app/body/service/AuthService';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SelectionService } from 'src/app/body/service/SelectionService';
import { Patient } from 'src/app/body/api/patient/Patient';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  providers: [ConfirmationService, MessageService],
})
export class AppTopbarComponent implements OnInit, OnDestroy {
  @ViewChild('menubutton') menuButton!: ElementRef;

  // Thème actuel (dim = sombre)
  darkTheme: boolean = this.layoutService.config().colorScheme === 'dim';

  // Texte affiché dans la topbar (infos patient)
  patientInfo: string = '';

  // Informations utilisateur connectées
  userName: string = '';
  userRole: string = '';

  // Abonnement aux changements de sélection de patient
  private patientSub!: Subscription;

  constructor(
    public layoutService: LayoutService,
    public authService: AuthService,
    private selectionService: SelectionService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    // Écoute des changements de sélection de patient via le SelectionService
    this.patientSub = this.selectionService.getPatientChanges().subscribe((patient: Patient | null) => {
      if (patient?.lastname && patient?.firstname && patient?.birthDate) {
        const birth = this.formatDate(patient.birthDate);
        this.patientInfo = `${patient.lastname} ${patient.firstname}  📅  ${birth}`;
      } else {
        // Si aucun patient sélectionné, on vide le bandeau
        this.patientInfo = '';
      }
    });

    // Récupération et affichage des infos utilisateur depuis le token JWT
    if (this.authService.isAuthenticate()) {
      const userInfo = this.authService.getUserInfoFromToken();
      this.userName = `${userInfo.firstname} ${userInfo.lastname}`;
      this.userRole = userInfo.role;
    }
  }

  // Formate une date au format JJ-MM-AAAA
  private formatDate(dateString: string): string {
    const date = new Date(dateString);
    return `${date.getDate().toString().padStart(2, '0')}-${(date.getMonth() + 1)
      .toString()
      .padStart(2, '0')}-${date.getFullYear()}`;
  }

  // Gestion du clic sur le bouton du menu
  onMenuButtonClick() {
    this.layoutService.onMenuToggle();
  }

  // Affiche le panneau de profil
  onProfileButtonClick() {
    this.layoutService.showProfileSidebar();
  }

  // Change le thème clair/sombre
  changeTheme() {
    const newColorScheme: ColorScheme = this.darkTheme ? 'light' : 'dim';
    this.layoutService.config.update((config) => ({
      ...config,
      colorScheme: newColorScheme,
    }));
    this.darkTheme = !this.darkTheme;
  }

  // Affiche la popup de confirmation et effectue le logout si confirmé
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

  // Nettoyage des abonnements lors de la destruction du composant
  ngOnDestroy(): void {
    if (this.patientSub) {
      this.patientSub.unsubscribe();
    }
  }
}
