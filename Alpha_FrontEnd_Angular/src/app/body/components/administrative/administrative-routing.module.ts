import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdministrativeComponent } from './administrative.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: AdministrativeComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class AdministrativesRoutingModule {}
