import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SlideListComponent } from './slide-list/slide-list.component';

const routes: Routes = [
  {
    path: 'list',
    component: SlideListComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SlideRoutingModule { }
