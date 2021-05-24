import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { UserListComponent } from './user-list/user-list.component';
import { RealmListComponent } from './realm-list/realm-list.component';
import { ShareModule } from '../share/share.module';
import { NoticeListComponent } from './notice-list/notice-list.component';
import { EditNoticeComponent } from './edit-notice/edit-notice.component';
import { EditUserComponent } from './edit-user/edit-user.component';

@NgModule({
  declarations: [
    UserListComponent,
    RealmListComponent,
    NoticeListComponent,
    EditNoticeComponent,
    EditUserComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ShareModule
  ]
})
export class AdminModule { }
