import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BackgroundComponent } from './background.component';
import { BackgroundsRoutingModule } from './background-routing.module';
import { SharedModule} from "../../app.body.module";
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';

@NgModule({
    declarations: [BackgroundComponent],
    imports: [
        CommonModule,
        FormsModule,
        BackgroundsRoutingModule,
        SharedModule,
        ButtonModule,
        MessagesModule,
    ],
})
export class BackgroundModule {}
