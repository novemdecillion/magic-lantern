import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MemberListComponent } from './member-list/member-list.component';
import { GroupListComponent } from './group-list/group-list.component';
import { GroupTreeComponent } from './group-tree/group-tree.component';
import { NextGenerationComponent } from './next-generation/next-generation.component';

const routes: Routes = [
  {
    path: 'list',
    component: GroupListComponent
  },
  {
    path: 'tree',
    component: GroupTreeComponent
  },
  {
    path: ':id/members',
    component: MemberListComponent
  },
  {
    path: 'next',
    component: NextGenerationComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GroupRoutingModule { }
