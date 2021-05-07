import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GroupRoutingModule } from './group-routing.module';
import { GroupListComponent } from './group-list/group-list.component';
import { ShareModule } from '../share/share.module';
import { GroupTreeComponent } from './group-tree/group-tree.component';
import { GroupPageComponent } from './group-page/group-page.component';
import { EditGroupComponent } from './edit-group/edit-group.component';
import { MemberListComponent } from './member-list/member-list.component';
import { EditMemberComponent } from './edit-member/edit-member.component';
import { AddMemberComponent } from './add-member/add-member.component';

@NgModule({
  declarations: [GroupListComponent, GroupTreeComponent, GroupPageComponent, EditGroupComponent, MemberListComponent, EditMemberComponent, AddMemberComponent],
  imports: [
    CommonModule,
    GroupRoutingModule,
    ShareModule
  ]
})
export class GroupModule { }
