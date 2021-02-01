import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudentRoutingModule } from './student-routing.module';
import { CourseListComponent } from './course-list/course-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';
import { MaterialModule } from '../material.module';

@NgModule({
  declarations: [CourseListComponent, SlideshowComponent],
  imports: [
    CommonModule,
    StudentRoutingModule,
    MaterialModule
  ]
})
export class StudentModule {
}
