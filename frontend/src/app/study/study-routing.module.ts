import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudyListComponent } from './study-list/study-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';

const routes: Routes = [
  {
    path: 'list',
    component: StudyListComponent
  },
  {
    path: 'slide/start/:slideId',
    component: SlideshowComponent
  },
  {
    path: 'slide/:studyId',
    component: SlideshowComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudyRoutingModule { }
