import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { finalize } from 'rxjs';
import { RegisterRequest } from 'src/app/body/api/auth/RegisterRequest';
import { AuthService } from 'src/app/body/service/AuthService';
import { ShareDataService } from 'src/app/body/service/ShareDataService';
import { NAVIGATION } from 'src/app/constants/app.constants';
import { LayoutService } from 'src/app/layout/service/app.layout.service';

@Component({
    templateUrl: './verification.component.html',
})
export class VerificationComponent implements OnInit {
    val1: number;
    val2: number;
    val3: number;
    val4: number;
    val5: number;
    val6: number;
    fullCode = '';
    loading: boolean = false;

    registerRequest: RegisterRequest;

    constructor(private layoutService: LayoutService,
        private messageService: MessageService,
        private authService: AuthService,
        private shareDataService : ShareDataService,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.registerRequest = this.shareDataService.getUserData();
    }

    get dark(): boolean {
        return this.layoutService.config().colorScheme !== 'light';
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

    validateEmail() {
        this.fullCode = `${this.val1 ?? ''}${this.val2 ?? ''}${this.val3 ?? ''}${this.val4 ?? ''}${this.val5 ?? ''}${this.val6 ?? ''}`;

        if (this.fullCode.length < 6) {
            this.messageService.add({
                severity: 'error',
                summary: 'Verification code',
                detail: 'The verification code is invalid.'
            });
        } else {
            this.loading = true;
            this.authService.verifyEmail(this.fullCode).pipe(
                finalize(() => this.loading = false)
            ).subscribe({
                next: result => {
                    this.router.navigate([NAVIGATION.SUCCESS]);
                }
            });
        }
    }
}
