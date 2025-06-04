import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
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
import { TooltipModule } from 'primeng/tooltip';




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
    MessagesModule,
    TooltipModule,
  ],
  providers: [DatePipe]
})
export class SummaryModule { }
