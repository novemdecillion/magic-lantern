import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NoticeListComponent } from './notice-list/notice-list.component';
import { RealmListComponent } from './realm-list/realm-list.component';
import { UserListComponent } from './user-list/user-list.component';

const routes: Routes = [
  {
    path: 'users',
    component: UserListComponent
  },
  {
    path: 'realms',
    component: RealmListComponent
  },
  {
    path: 'notices',
    component: NoticeListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
