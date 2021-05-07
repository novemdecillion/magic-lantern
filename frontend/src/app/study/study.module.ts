import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudyRoutingModule } from './study-routing.module';
import { LessonListComponent } from './lesson-list/lesson-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';
import { ShareModule } from '../share/share.module';

@NgModule({
  declarations: [LessonListComponent, SlideshowComponent],
  imports: [
    CommonModule,
    StudyRoutingModule,
    ShareModule
  ]
})
export class StudentModule {
}
