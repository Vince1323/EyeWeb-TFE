import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ExamComponent } from './exam.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: ExamComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class ExamsRoutingModule {}
