import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {AuthGuard} from "../../guards/AuthGuard";

@NgModule({
    imports: [RouterModule.forChild([
        {
            path: 'patients/:idPatient/summary',
            loadChildren: () =>
              import('./summary/summary.module').then((m) => m.SummaryModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Summary' }
            
          },
          
        {
            path: 'patients/:idPatient/biometrics',
            loadChildren: () =>
                import(
                    './biometry/biometry.module'
                    ).then((m) => m.BiometryModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Biometry' }
        },
        {
            path: 'patients/:idPatient/preoperative',
            loadChildren: () =>
                import(
                    './preoperative/preoperative.module'
                    ).then((m) => m.PreoperativeModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Preoperative' }
        },
        {
            path: 'patients/:idPatient/calculators',
            loadChildren: () =>
                import(
                    './calculator/calculator.module'
                    ).then((m) => m.CalculatorModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'IOL Calculator' }
        },
        {
            path: 'patients/:idPatient/planning',
            loadChildren: () =>
                import(
                    './planning/planning.module'
                    ).then((m) => m.PlanningModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Planning' }
        },
        {
            path: 'patients/:idPatient/protocol',
            loadChildren: () =>
                import(
                    './protocol/protocol.module'
                    ).then((m) => m.ProtocolModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Protocol' }
        },
        {
            path: 'patients/:idPatient/postoperative',
            loadChildren: () =>
                import(
                    './postoperative/postoperative.module'
                    ).then((m) => m.PostoperativeModule),
            canActivate: [AuthGuard],
            data: { breadcrumb: 'Postoperative' }
        },
        { path: '**', redirectTo: '/notfound' }
    ])],
    exports: [RouterModule]
})
export class CataractRoutingModule {}
