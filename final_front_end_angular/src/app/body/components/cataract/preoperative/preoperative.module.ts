import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PreoperativeComponent } from './preoperative.component';
import { PreoperativeRoutingModule } from './preoperative-routing.module';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputMaskModule } from 'primeng/inputmask';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import {SharedModule} from "../../../app.body.module";
import { TooltipModule } from 'primeng/tooltip';


@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        PreoperativeRoutingModule,
        InputTextModule,
        DropdownModule,
        InputTextareaModule,
        InputMaskModule,
        CalendarModule,
        ButtonModule,
        MessagesModule,
        SharedModule,
        TooltipModule,
    ],
    declarations: [PreoperativeComponent],
})
export class PreoperativeModule {}
