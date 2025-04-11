import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ProtocolComponent } from './protocol.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: ProtocolComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class ProtocolRoutingModules {}
