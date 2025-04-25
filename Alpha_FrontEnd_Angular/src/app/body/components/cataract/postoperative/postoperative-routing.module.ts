import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PostoperativeComponent } from './postoperative.component';

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: PostoperativeComponent },
        ]),
    ],
    exports: [RouterModule],
})
export class PostoperativeRoutingModules {}
