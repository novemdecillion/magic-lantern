import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LessonListComponent } from './lesson-list/lesson-list.component';

const routes: Routes = [
  {
    path: 'list',
    component: LessonListComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LessonRoutingModule { }
