import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PlanningComponent } from './planning.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: PlanningComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class PlanningsRoutingModule {}
