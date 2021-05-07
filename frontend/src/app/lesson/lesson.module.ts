import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LessonRoutingModule } from './lesson-routing.module';
import { LessonListComponent } from './lesson-list/lesson-list.component';
import { ShareModule } from '../share/share.module';
import { AddLessonComponent } from './add-lesson/add-lesson.component';

@NgModule({
  declarations: [LessonListComponent, AddLessonComponent],
  imports: [
    CommonModule,
    LessonRoutingModule,
    ShareModule
  ]
})
export class LessonModule { }
