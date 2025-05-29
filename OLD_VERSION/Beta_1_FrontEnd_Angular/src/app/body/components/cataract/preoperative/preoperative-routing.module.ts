import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PreoperativeComponent } from './preoperative.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: PreoperativeComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class PreoperativeRoutingModule {}
