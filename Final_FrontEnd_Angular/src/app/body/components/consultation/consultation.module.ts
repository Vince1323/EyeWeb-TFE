import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConsultationComponent } from './consultation.component';
import { ConsultationsRoutingModule } from './consultation-routing.module';

import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';

@NgModule({
  declarations: [ConsultationComponent],
  imports: [
    CommonModule,
    FormsModule,
    ConsultationsRoutingModule,
    InputTextModule,
    InputTextareaModule, 
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
    CalendarModule
  ]
})
export class ConsultationModule {}
