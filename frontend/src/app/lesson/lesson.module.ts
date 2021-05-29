import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LessonRoutingModule } from './lesson-routing.module';
import { LessonListComponent } from './lesson-list/lesson-list.component';
import { ShareModule } from '../share/share.module';
import { AddLessonComponent } from './add-lesson/add-lesson.component';
import { StudentListComponent } from './student-list/student-list.component';
import { StudentStatusComponent } from './student-status/student-status.component';

@NgModule({
  declarations: [LessonListComponent, AddLessonComponent, StudentListComponent, StudentStatusComponent],
  imports: [
    CommonModule,
    LessonRoutingModule,
    ShareModule
  ]
})
export class LessonModule { }
