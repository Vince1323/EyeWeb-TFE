import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { UserService } from '../../service/UserService';
import { User } from '../../api/auth/User';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss'],
    providers: [MessageService]
})
export class ProfileComponent implements OnInit {
    messages: any[] = [];
    user: User | null = null;
    isEditMode: boolean = false;
    originalUser: User | null = null;
    userForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private messageService: MessageService,
        private userService: UserService
    ) {
        this.userForm = this.fb.group({
            firstname: ['', [Validators.required, Validators.pattern("^[A-zÀ-ú-]+(?:['-\\s][A-zÀ-ú-]+)*$")]],
            lastname: ['', [Validators.required, Validators.pattern("^[A-zÀ-ú-]+(?:['-\\s][A-zÀ-ú-]+)*$")]],
            email: ['', [Validators.required, Validators.email]],
            phone: ['', [Validators.required, Validators.pattern("^[0-9\\s+()-]{8,15}$")]],
            birthDate: [null],  

            address: this.fb.group({
                street: ['', Validators.required],
                streetNumber: ['', [Validators.required, Validators.pattern("^[A-z0-9/-]+$")]],
                boxNumber: ['', Validators.pattern("^[A-z0-9/-]*$")], 
                city: ['', [Validators.required, Validators.pattern("^[A-zÀ-ú\\s-]+$")]],
                zipCode: ['', [Validators.required, Validators.pattern("^\\d+$")]],
                country: ['', [Validators.required, Validators.pattern("^[A-zÀ-ú\\s-]+$")]] 
            })
        });
        // Désactiver les champs initialement
        this.userForm.disable();
    }

    ngOnInit() {
        this.loadUser();
    }

    loadUser() {
        this.userService.getCurrentUser().subscribe({
            next: (user) => {
                let birthDateValue: Date | null = user.birthDate ? new Date(user.birthDate) : null;
                this.user = user;
                this.originalUser = JSON.parse(JSON.stringify(user));

                // Correction pour éviter `null` sur boxNumber et country
                this.userForm.patchValue({
                    ...user,
                    birthDate: birthDateValue,
                    address: {
                        ...user.address,
                        boxNumber: user.address.boxNumber || '', 
                        country: user.address.country || ''
                    }
                });
            },
            error: () => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Failed to load user data.'
                });
            }
        });
    }

    toggleEditMode() {
        if (this.isEditMode) {
            this.userForm.patchValue(this.originalUser);
            this.userForm.disable();
        } else {
            this.originalUser = JSON.parse(JSON.stringify(this.userForm.value));
            this.userForm.enable();
        }
        this.isEditMode = !this.isEditMode;
    }

    updateUser() {
        if (this.userForm.invalid) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Invalid data, please check the fields.'
            });
            return;
        }

        let updatedUser = { ...this.user, ...this.userForm.value };

        if (updatedUser.birthDate instanceof Date) {
            updatedUser.birthDate = updatedUser.birthDate.toISOString().split('T')[0];
        }

        this.userService.updateVerifiedUser(updatedUser).subscribe({
            next: (user) => {
                this.user = user;
                this.isEditMode = false;
                this.userForm.disable();

                this.messages = [
                    { severity: 'success', detail: `The user ${user.firstname} ${user.lastname} has been successfully updated.` }
                ];
                setTimeout(() => {
                    this.messages = [];
                }, 5000);
            },
            error: () => {
                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Failed to update user.'
                });
            }
        });
    }
}
