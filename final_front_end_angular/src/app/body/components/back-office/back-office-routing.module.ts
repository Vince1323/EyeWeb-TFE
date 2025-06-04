import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BackOfficeComponent } from './back-office.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: BackOfficeComponent }]),
    ],
    exports: [RouterModule],
})

export class BackOfficeRoutingModule {

}

