import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SummaryComponent } from './summary.component';
import { SummaryRoutingModule } from './summary-routing.module';

import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { MessagesModule } from 'primeng/messages';


@NgModule({
  declarations: [SummaryComponent],
  imports: [
    CommonModule,
    FormsModule,
    SummaryRoutingModule,
    ToastModule,
    ConfirmDialogModule,
    CalendarModule,
    DropdownModule,
    InputTextModule,
    RadioButtonModule,
    ButtonModule,
    CheckboxModule,
    MessagesModule
  ]
})
export class SummaryModule { }
