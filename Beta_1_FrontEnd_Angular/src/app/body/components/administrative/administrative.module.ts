import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdministrativeComponent } from './administrative.component';
import { AdministrativesRoutingModule } from './administrative-routing.module';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputMaskModule } from 'primeng/inputmask';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { CardModule } from 'primeng/card';



@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        AdministrativesRoutingModule,
        InputTextModule,
        DropdownModule,
        InputTextareaModule,
        InputMaskModule,
        CalendarModule,
        ButtonModule,
        MessagesModule,
        ReactiveFormsModule,
        FormsModule,
        ToastModule,
        ConfirmDialogModule,
        CardModule
    ],
    declarations: [AdministrativeComponent],
})
export class AdministrativeModule {}
