import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GroupListComponent } from './group-list/group-list.component';
import { GroupTreeComponent } from './group-tree/group-tree.component';

const routes: Routes = [
  {
    path: 'groups',
    component: GroupListComponent
  },
  {
    path: 'group-tree',
    component: GroupTreeComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ManagerRoutingModule { }
