import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LessonListComponent } from './lesson-list/lesson-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';

const routes: Routes = [
  {
    path: 'lessons',
    component: LessonListComponent
  },
  {
    path: 'slideshow/:courseId',
    component: SlideshowComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudyRoutingModule { }
