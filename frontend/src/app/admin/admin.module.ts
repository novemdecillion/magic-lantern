import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { UserListComponent } from './user-list/user-list.component';
import { MaterialModule } from '../material/material.module';
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
    MaterialModule,
    ShareModule
  ]
})
export class AdminModule { }
