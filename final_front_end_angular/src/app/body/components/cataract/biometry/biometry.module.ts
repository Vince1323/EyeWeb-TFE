import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BiometryComponent } from './biometry.component';
import { ChartModule } from 'primeng/chart';
import { MenuModule } from 'primeng/menu';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { StyleClassModule } from 'primeng/styleclass';
import { PanelMenuModule } from 'primeng/panelmenu';
import { BiometricsRoutingModule } from './biometry-routing.module';
import { TagModule } from 'primeng/tag';
import { MultiSelectModule } from 'primeng/multiselect';
import { InputSwitchModule } from 'primeng/inputswitch';
import { DropdownModule } from 'primeng/dropdown';
import { SharedModule} from "../../../app.body.module";
import { MessagesModule } from 'primeng/messages';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ChartModule,
        MenuModule,
        TableModule,
        StyleClassModule,
        PanelMenuModule,
        ButtonModule,
        BiometricsRoutingModule,
        TagModule,
        MultiSelectModule,
        InputSwitchModule,
        DropdownModule,
        SharedModule,
        MessagesModule,
        DialogModule,
        ToastModule,
        ConfirmDialogModule
    ],
    declarations: [BiometryComponent],
})
export class BiometryModule {}
