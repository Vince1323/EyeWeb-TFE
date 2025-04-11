import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ExamComponent } from './exam.component';
import { ExamsRoutingModule } from './exam-routing.module';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputMaskModule } from 'primeng/inputmask';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ExamsRoutingModule,
        InputTextModule,
        DropdownModule,
        InputTextareaModule,
        InputMaskModule,
        CalendarModule,
        ButtonModule,
        MessagesModule,
    ],
    declarations: [ExamComponent],
})
export class ExamModule {}
