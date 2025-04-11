import { NgModule } from '@angular/core';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { AppLayoutComponent } from './layout/app.layout.component';
import { AuthGuard } from './body/guards/AuthGuard';
import { AccessGuard } from './body/guards/AccessGuard';

const routerOptions: ExtraOptions = {
    anchorScrolling: 'enabled',
};

const routes: Routes = [
    {
        path: '',
        component: AppLayoutComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: '',
                loadChildren: () =>
                    import('./body/components/dashboard/dashboard.module').then(
                        (m) => m.DashboardModule
                    ),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Home' }
            },
            {
                path: 'patients/:idPatient/exam',
                loadChildren: () =>
                    import(
                        './body/components/exam/exam.module'
                        ).then((m) => m.ExamModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Exam' }
            },
            {
                path: 'patients/:idPatient/consultation',
                loadChildren: () =>
                    import(
                        './body/components/consultation/consultation.module'
                        ).then((m) => m.ConsultationModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Consultation' }
            },
            {
                path: 'patients/:idPatient/administrative',
                loadChildren: () =>
                    import(
                        './body/components/administrative/administrative.module'
                        ).then((m) => m.AdministrativeModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Patient Informations' }
            },
            {
                path: 'patients/:idPatient/background',
                loadChildren: () =>
                    import(
                        './body/components/background/background.module'
                        ).then((m) => m.BackgroundModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Medical Background' }
            },
            {
                path: 'patients/:idPatient/summary',
                loadChildren: () =>
                    import('./body/components/cataract/summary/summary.module').then(
                        (m) => m.SummaryModule
                    ),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Summary' }
            },            
            {
                path: 'cataract',
                loadChildren: () =>
                    import(
                        './body/components/cataract/cataract.module'
                        ).then((m) => m.CataractModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Cataract' }
            },
            {
                path: 'contact',
                loadChildren: () =>
                    import(
                        './body/components/contact/contact.module'
                        ).then((m) => m.ContactModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Contact' }
            },
            {
                path: 'faq',
                loadChildren: () =>
                    import(
                        './body/components/faq/faq.module'
                        ).then((m) => m.FaqModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'FAQ' }
            },
            {
                path: 'profile',
                loadChildren: () =>
                    import(
                        './body/components/profile/profile.module'
                        ).then((m) => m.ProfileModule),
                canActivate: [AuthGuard],
                data: { breadcrumb: 'Profile' }
            },
            {
                path: 'back-office',
                loadChildren: () =>
                    import(
                        './body/components/back-office/back-office.module'
                        ).then((m) => m.BackOfficeModule),
                canActivate: [AccessGuard],
                data: { breadcrumb: 'Back-Office' }
            },
        ],
    },
    {
        path: 'auth',
        data: { breadcrumb: 'Auth' },
        loadChildren: () =>
            import('./body/components/auth/auth.module').then(
                (m) => m.AuthModule
            ),
    },
    {
        path: 'notfound',
        loadChildren: () =>
            import('./body/components/notfound/notfound.module').then(
                (m) => m.NotfoundModule
            ),
    },
    { path: '**', redirectTo: '/notfound' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
