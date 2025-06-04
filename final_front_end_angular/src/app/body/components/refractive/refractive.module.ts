import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RefractiveComponent } from './refractive.component';
import { RefractiveRoutingModule } from './refractive-routing.module';

import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';

@NgModule({
  declarations: [RefractiveComponent],
  imports: [
    CommonModule,
    FormsModule,
    RefractiveRoutingModule,
    InputTextModule,
    InputTextareaModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
    CalendarModule
  ]
})
export class RefractiveModule {}
