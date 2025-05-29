import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ExamComponent } from './exam.component';
import { ExamRoutingModule } from './exam-routing.module';

import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@NgModule({
  declarations: [ExamComponent],
  imports: [
    CommonModule,
    FormsModule,
    ExamRoutingModule,
    InputTextModule,
    InputTextareaModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule
  ]
})
export class ExamModule {}
