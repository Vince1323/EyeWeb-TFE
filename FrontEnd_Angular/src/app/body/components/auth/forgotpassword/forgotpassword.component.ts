import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { ResetPasswordRequest } from 'src/app/body/api/auth/ResetPasswordRequest';
import { ShareDataService } from 'src/app/body/service/ShareDataService';
import { UserService } from 'src/app/body/service/UserService';
import { NAVIGATION } from 'src/app/constants/app.constants';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import {AuthService} from "../../../service/AuthService";

@Component({
    templateUrl: './forgotpassword.component.html',
})
export class ForgotPasswordComponent implements OnInit {
    emailConfirmationForm: FormGroup;
    loading: boolean = false;
    requestResetPassword = {} as ResetPasswordRequest;

    constructor(private layoutService: LayoutService,
        private fb: FormBuilder,
        private userService: UserService,
        private authService: AuthService,
        private router: Router,
        private messageService: MessageService,
        private shareDataService: ShareDataService
    ) {}

    get dark(): boolean {
        return this.layoutService.config().colorScheme !== 'light';
    }

    ngOnInit(): void {
        this.initializeForm();
    }

    initializeForm() {
        this.emailConfirmationForm = this.fb.group({
            email: ['', [Validators.required, Validators.email]]
        });
    }

    sendEmailCode() {
        this.loading = true;
        if(this.emailConfirmationForm.value.email !== null) {
            this.requestResetPassword.email = this.emailConfirmationForm.value.email;
            this.authService.sendCodeResetPassword(this.requestResetPassword).pipe(
                finalize(() => this.loading = false)
            ).subscribe({
                next: (response) => {
                    this.shareDataService.setUserData(this.requestResetPassword);
                    this.router.navigate([NAVIGATION.NEWPASSWORD]);
                }
            });
        }
    }
}
