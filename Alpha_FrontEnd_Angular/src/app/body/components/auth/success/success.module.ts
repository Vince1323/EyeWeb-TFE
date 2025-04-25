import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { SuccessComponent } from './success.component';
import { SuccessRoutingModule } from './success-routing.module';

@NgModule({
    imports: [
        CommonModule,
        SuccessRoutingModule,
        ButtonModule
    ],
    declarations: [SuccessComponent]
})
export class SuccessModule {}
