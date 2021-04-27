import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerRoutingModule } from './manager-routing.module';
import { GroupListComponent } from './group-list/group-list.component';
import { ShareModule } from '../share/share.module';
import { GroupTreeComponent } from './group-tree/group-tree.component';
import { GroupPageComponent } from './group-page/group-page.component';
import { EditGroupComponent } from './edit-group/edit-group.component';

@NgModule({
  declarations: [GroupListComponent, GroupTreeComponent, GroupPageComponent, EditGroupComponent],
  imports: [
    CommonModule,
    ManagerRoutingModule,
    ShareModule
  ]
})
export class ManagerModule { }
