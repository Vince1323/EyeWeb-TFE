import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BiometryComponent } from './biometry.component';

@NgModule({
    imports: [
        RouterModule.forChild([{ path: '', component: BiometryComponent }]),
    ],
    exports: [RouterModule],
})
export class BiometricsRoutingModule {}
