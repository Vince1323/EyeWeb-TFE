import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterRoutingModule } from './register-routing.module';
import { RegisterComponent } from './register.component';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { AppConfigModule } from 'src/app/layout/config/app.config.module';
import { PasswordModule } from 'primeng/password';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { DialogModule } from 'primeng/dialog';
import { SharedModule } from 'src/app/body/app.body.module';
import { CalendarModule } from 'primeng/calendar';

@NgModule({
    imports: [
        CommonModule,
        RegisterRoutingModule,
        FormsModule,
        ButtonModule,
        RippleModule,
        InputTextModule,
        CheckboxModule,
        AppConfigModule,
        PasswordModule,
        ToastModule,
        ReactiveFormsModule,
        DividerModule,
        DialogModule,
        SharedModule,
        CalendarModule
    ],
    declarations: [RegisterComponent]
})
export class RegisterModule { }
