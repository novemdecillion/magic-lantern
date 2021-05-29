import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LessonListComponent } from './lesson-list/lesson-list.component';
import { StudentListComponent } from './student-list/student-list.component';
import { StudentStatusComponent } from './student-status/student-status.component';

const routes: Routes = [
  {
    path: 'list',
    component: LessonListComponent
  },
  {
    path: 'list/:lessonId',
    component: StudentListComponent
  },
  {
    path: 'status/:studyId',
    component: StudentStatusComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LessonRoutingModule { }
