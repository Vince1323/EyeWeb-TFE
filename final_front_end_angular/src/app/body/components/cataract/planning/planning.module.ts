import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlanningComponent } from './planning.component';
import { PlanningsRoutingModule } from './planning-routing.module';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputMaskModule } from 'primeng/inputmask';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        PlanningsRoutingModule,
        InputTextModule,
        DropdownModule,
        InputTextareaModule,
        InputMaskModule,
        CalendarModule,
        ButtonModule,
        ToastModule,               
        ConfirmDialogModule,       
        MessagesModule,
        TooltipModule,
    ],
    declarations: [PlanningComponent],
})
export class PlanningModule {}
