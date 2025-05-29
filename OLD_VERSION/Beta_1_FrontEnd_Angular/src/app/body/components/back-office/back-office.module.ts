import { NgModule } from '@angular/core';
import { BackOfficeComponent } from './back-office.component';
import { CommonModule } from '@angular/common';
import { BackOfficeRoutingModule } from './back-office-routing.module';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ProgressBarModule } from "primeng/progressbar";
import { DialogModule } from "primeng/dialog";
import { AutoCompleteModule } from "primeng/autocomplete";
import { FileUploadModule } from "primeng/fileupload";
import { MultiSelectModule } from "primeng/multiselect";
import { SelectButtonModule } from "primeng/selectbutton";
import { SharedModule } from "../../app.body.module";
import { StepsModule } from "primeng/steps";
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { CheckboxModule } from 'primeng/checkbox';


@NgModule({
    imports: [
        CommonModule,
        BackOfficeRoutingModule,
        MessagesModule,
        ToastModule,
        TableModule,
        ButtonModule,
        ConfirmDialogModule,
        ProgressBarModule,
        DialogModule,
        AutoCompleteModule,
        FileUploadModule,
        MultiSelectModule,
        SelectButtonModule,
        SharedModule,
        StepsModule,
        FormsModule,
        InputTextModule,
        DropdownModule,
        CheckboxModule,
    ],
    declarations: [BackOfficeComponent],
})
export class BackOfficeModule {}
