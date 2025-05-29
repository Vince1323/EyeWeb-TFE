import { Component, OnInit } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { finalize } from "rxjs";
import { Address } from "src/app/body/api/Address";
import { AuthRequest } from "src/app/body/api/auth/AuthRequest";
import { RegisterRequest } from "src/app/body/api/auth/RegisterRequest";
import { AuthService } from "src/app/body/service/AuthService";
import { ShareDataService } from "src/app/body/service/ShareDataService";
import { NAVIGATION } from "src/app/constants/app.constants";
import { LayoutService } from "src/app/layout/service/app.layout.service";

@Component({
    templateUrl: "./register.component.html",
})
export class RegisterComponent implements OnInit {
    registerRequest = {} as RegisterRequest;
    address = {} as Address;
    request = {} as AuthRequest;
    confirmPassword: string;
    loading: boolean = false;
    fullCode = "";
    isReadTerms: boolean = false;
    minDate: Date = new Date('01-01-1900');
    maxDate: Date = new Date();

    constructor(
        public layoutService: LayoutService,
        private authService: AuthService,
        private fb: FormBuilder,
        private messageService: MessageService,
        private shareDataService: ShareDataService,
        private router: Router
    ) {}

    get dark(): boolean {
        return this.layoutService.config().colorScheme !== "light";
    }

    ngOnInit(): void {
        this.registerRequest.address = this.address;
    }

    registerForm = this.fb.group({
        firstname: ["", [Validators.required, Validators.pattern("[A-zÀ-ú-]+(?:['-\\s][[A-zÀ-ú-]+)*")]],
        lastname: ["", [Validators.required, Validators.pattern("[A-zÀ-ú-]+(?:['-\\s][[A-zÀ-ú-]+)*")]],
        birthDate: ["", [Validators.required]],
        phone: ["", [Validators.required, Validators.pattern("((\\+32)?[0-9][0-9]{6,12}$)")]],
        address: this.fb.group({
            country: ["", [Validators.required]],
            city: ["", [Validators.required]],
            zipCode: ["", [Validators.required]],
            street: ["", [Validators.required]],
            streetNumber: ["", [Validators.required]],
            boxNumber: ["", [Validators.nullValidator]],
        }),
        email: ["", [Validators.required, Validators.email]],
        password: ["", [Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$")]],
        confirmPassword: ["", [Validators.required]],
        hasReadTermsAndConditions: [false, Validators.requiredTrue],
    });

    saveRegister() {
        this.registerRequest = this.registerForm.value as RegisterRequest;
        if (this.registerRequest.password !== this.registerForm.get("confirmPassword").value) {
            this.messageService.add({
                severity: "error",
                summary: "Confirm password",
                detail: "Confirmation password not the same as password.",
            });
        } else {
            this.loading = true;
            this.authService
                .register(this.registerRequest)
                .pipe(finalize(() => (this.loading = false)))
                .subscribe({
                    next: (result) => {
                        this.shareDataService.setUserData(this.registerRequest);
                        this.router.navigate([NAVIGATION.VERIFICATION]);
                    },
                });
        }
    }

    displayTerms(value: boolean) {
        this.isReadTerms = value;
    }

    regeneratesCodeValidationEmail() {
        this.request.email = this.registerRequest.email;
        this.authService.regeneratesCodeValidationEmail(this.request).subscribe((result) => {
            this.messageService.add({
                severity: "success",
                summary: "Regenerates code",
                detail: "The verification code has been send.",
            });
        });
    }
}
