import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GlaucomaComponent } from './glaucoma.component';
import { GlaucomaRoutingModule } from './glaucoma-routing.module';

import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar'; 

@NgModule({
  declarations: [GlaucomaComponent],
  imports: [
    CommonModule,
    FormsModule,
    GlaucomaRoutingModule,
    InputTextModule,
    InputTextareaModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
    CalendarModule 
  ]
})
export class GlaucomaModule {}
