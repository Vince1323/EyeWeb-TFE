import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProtocolComponent } from './protocol.component';
import { ProtocolRoutingModules } from './protocol-routing.module';
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
import { CommonModule, DatePipe } from '@angular/common';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ProtocolRoutingModules,
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
    declarations: [ProtocolComponent],
    providers: [DatePipe]
})
export class ProtocolModule {}
