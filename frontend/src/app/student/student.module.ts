import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentRoutingModule } from './student-routing.module';
import { CourseListComponent } from './course-list/course-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';
import { ShareModule } from '../share/share.module';

@NgModule({
  declarations: [CourseListComponent, SlideshowComponent],
  imports: [
    CommonModule,
    StudentRoutingModule,
    ShareModule
  ]
})
export class StudentModule {
}
