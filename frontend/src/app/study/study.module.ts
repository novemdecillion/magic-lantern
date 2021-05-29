import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StudyRoutingModule } from './study-routing.module';
import { StudyListComponent } from './study-list/study-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';
import { ShareModule } from '../share/share.module';
import { StudyStatusComponent } from './study-status/study-status.component';

@NgModule({
  declarations: [StudyListComponent, SlideshowComponent, StudyStatusComponent],
  imports: [
    CommonModule,
    StudyRoutingModule,
    ShareModule
  ]
})
export class StudentModule {
}
