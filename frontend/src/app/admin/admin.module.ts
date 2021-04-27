import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { UserListComponent } from './user-list/user-list.component';
import { RealmListComponent } from './realm-list/realm-list.component';
import { ShareModule } from '../share/share.module';

@NgModule({
  declarations: [
    UserListComponent,
    RealmListComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ShareModule
  ]
})
export class AdminModule { }
