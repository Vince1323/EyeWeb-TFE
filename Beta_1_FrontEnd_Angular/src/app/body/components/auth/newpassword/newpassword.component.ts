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
    templateUrl: './newpassword.component.html',
})
export class NewPasswordComponent implements OnInit {
    rememberMe: boolean = false;
    requestResetPassword = {} as ResetPasswordRequest;
    val1: number;
    val2: number;
    val3: number;
    val4: number;
    val5: number;
    val6: number;
    fullCode = '';
    loading: boolean = false;
    resetPasswordForm: FormGroup;

    constructor(
        private layoutService: LayoutService,
        private shareDataService: ShareDataService,
        private messageService: MessageService,
        private fb: FormBuilder,
        private router: Router,
        private userService: UserService,
        private authService: AuthService,
    ) {}

    get dark(): boolean {
        return this.layoutService.config().colorScheme !== 'light';
    }

    ngOnInit(): void {
        this.requestResetPassword = this.shareDataService.getUserData();
        this.initializeForm();
    }

    initializeForm() {
        this.resetPasswordForm = this.fb.group({
            email: [this.requestResetPassword?.email],
            resetPassword: [
                '',
                [
                    Validators.required,
                    Validators.pattern(
                        '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$'
                    ),
                ],
            ],
            confirmPassword: ['', [Validators.required]],
        });
    }

    onDigitInput(event: any) {
        let element;
        if (event.code !== 'Backspace')
            if (event.code.includes('Numpad') || event.code.includes('Digit')) {
                element = event.srcElement.nextElementSibling;
            }
        if (event.code === 'Backspace')
            element = event.srcElement.previousElementSibling;

        if (element == null) return;
        else element.focus();
    }

    saveResetPassword() {
        this.fullCode = `${this.val1 ?? ''}${this.val2 ?? ''}${this.val3 ?? ''}${this.val4 ?? ''}${this.val5 ?? ''}${this.val6 ?? ''}`;

        if (this.fullCode.length < 6) {
            this.messageService.add({
                severity: 'error',
                summary: 'Verification code',
                detail: 'The verification code is invalid.'
            });
        } else {
            if (this.resetPasswordForm.get('resetPassword').value !== this.resetPasswordForm.get('confirmPassword').value) {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Confirm password',
                    detail: 'Confirmation password not the same as password.'
                });
            } else {
                this.loading = true;
                this.requestResetPassword = this.resetPasswordForm.value as ResetPasswordRequest;
                this.requestResetPassword.code = this.fullCode;
                this.authService.resetPassword(this.requestResetPassword).pipe(
                    finalize(() => this.loading = false)
                ).subscribe(
                    (result) => {
                        this.router.navigate([NAVIGATION.SUCCESS], { state: { caller: NewPasswordComponent.name } });
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Password reset',
                            detail: 'Password has been reset',
                        });
                    }
                );
            }
        }
    }
}
