import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GlaucomaComponent } from './glaucoma.component';

const routes: Routes = [
  {
    path: '',
    component: GlaucomaComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GlaucomaRoutingModule {}
