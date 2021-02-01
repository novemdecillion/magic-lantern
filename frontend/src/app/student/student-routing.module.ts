import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CourseListComponent } from './course-list/course-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';

const routes: Routes = [
  {
    path: 'courses',
    component: CourseListComponent
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
export class StudentRoutingModule { }
