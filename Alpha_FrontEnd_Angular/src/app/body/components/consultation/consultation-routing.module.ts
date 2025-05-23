import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ConsultationComponent } from './consultation.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: ConsultationComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class ConsultationsRoutingModule {}
