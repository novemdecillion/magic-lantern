import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './root/home/home.component';

const routes: Routes = [
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'group',
    loadChildren: () => import('./group/group.module').then(m => m.GroupModule)
  },
  {
    path: 'slide',
    loadChildren: () => import('./slide/slide.module').then(m => m.SlideModule)
  },
  {
    path: 'lesson',
    loadChildren: () => import('./lesson/lesson.module').then(m => m.LessonModule)
  },
  {
    path: 'study',
    loadChildren: () => import('./study/study.module').then(m => m.StudentModule)
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
