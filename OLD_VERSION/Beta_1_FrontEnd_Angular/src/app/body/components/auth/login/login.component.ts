import { Component } from '@angular/core';
import { AuthRequest } from 'src/app/body/api/auth/AuthRequest';
import { User } from 'src/app/body/api/auth/User';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AuthService } from 'src/app/body/service/AuthService';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { StorageService} from "../../../service/StorageService";
import { OrganizationService } from 'src/app/body/service/OrganizationService';
import { Router} from "@angular/router";
import {NAVIGATION} from 'src/app/constants/app.constants';
import { finalize } from 'rxjs';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styles: [
        `
            :host ::ng-deep .pi-eye,
            :host ::ng-deep .pi-eye-slash {
                transform: scale(1.6);
                margin-right: 1rem;
                color: var(--primary-color) !important;
            }
        `,
    ],
})
export class LoginComponent {
    rememberMe: boolean = false;
    loading: boolean = false;

    loginForm = this.fb.group({
        email: ['', [Validators.required]],
        password: ['', [Validators.required]],
    });

    conditionsModal: boolean = false;
    submitted: boolean = false;
    request = {} as AuthRequest;
    user = {} as User;

    idOrga!: number[];
    organizations: any;

    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private storageService: StorageService,
        private fb: FormBuilder,
        private messageService: MessageService,
        private organizationService: OrganizationService,
        private router: Router
    ) {}

    get dark(): boolean {
        return this.layoutService.config().colorScheme !== 'light';
    }

    login() {
        this.submitted = false;

        // Validate the form
        if (!this.isLoginFormValid()) {
            this.submitted = true;
            return;
        }

        // Extract email and password
        const { email, password } = this.getEmailAndPassword();
        if (!email || !password) {
            this.messageService.add({
                severity: 'error',
                summary: 'VALIDATION_ERROR',
                detail: 'Email or password cannot be empty'
            });
            return;
        }

        // Prepare the request object
        this.prepareAuthenticationRequest(email, password);

        // Authenticate the user
        this.authenticateUser();
    }

    // Function to validate the login form
    private isLoginFormValid(): boolean {
        return this.loginForm.valid;
    }

    // Function to get and format email and password
    private getEmailAndPassword(): { email: string; password: string } {
        const email = this.loginForm.value.email?.toLowerCase() ?? '';
        const password = this.loginForm.value.password ?? '';
        return { email, password };
    }

    // Function to prepare the request for authentication
    private prepareAuthenticationRequest(
        email: string,
        password: string
    ): void {
        this.request.email = email;
        this.request.password = password;
    }

    // Function to handle user authentication
    private authenticateUser(): void {
        this.loading = true;
        this.authService.authenticate(this.request).pipe(
            finalize(() => this.loading = false)
        ).subscribe({
            next: (result) => {
                this.handleSuccessfulAuthentication();
            }
        });
    }

    // Function to handle successful authentication
    private handleSuccessfulAuthentication(): void {
        this.user = this.authService.getUserInfoFromToken();
        if (this.user.hasReadTermsAndConditions) {
            this.loadUserOrganizations();
        } else {
            this.conditionsModal = true;
        }
    }

    // Function to load user organizations
    private loadUserOrganizations(): void {
        this.organizationService
            .getOrganizationsByUserId(this.user.id)
            .subscribe((result) => {
                this.organizations = result;
                this.storeOrganizationsAndWorkspaces();
                this.router.navigate([``]);
            });
    }

    // Function to store organizations and workspaces in local storage
    private storeOrganizationsAndWorkspaces(): void {
        const organizationIds = this.organizations.map(
            (org: { id: any }) => org.id
        );
        this.storageService.set('organization', organizationIds);

        const workspaces = this.organizations.map(
            (org: { name: any }) => org.name
        );
        this.storageService.set('workspace', workspaces);
    }

    goToRegister() {
        this.router.navigate([NAVIGATION.REGISTER]);
    }
    goToForgotPassword() {
        this.router.navigate([NAVIGATION.FORGOTPASSWORD]);
    }

    logout() {
        this.authService.logout();
        this.conditionsModal = false;
    }

}

