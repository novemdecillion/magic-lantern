import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StudyListComponent } from './study-list/study-list.component';
import { SlideshowComponent } from './slideshow/slideshow.component';
import { StudyStatusComponent } from './study-status/study-status.component';

const routes: Routes = [
  {
    path: 'list',
    component: StudyListComponent
  },
  // {
  //   path: 'status/slide/:slideId',
  //   component: StudyStatusComponent
  // },
  // {
  //   path: 'status/:studyId',
  //   component: StudyStatusComponent
  // },
  {
    path: 'status/:slideId',
    component: StudyStatusComponent
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
