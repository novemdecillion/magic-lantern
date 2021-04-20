import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagerRoutingModule } from './manager-routing.module';
import { GroupListComponent } from './group-list/group-list.component';
import { MaterialModule } from '../material/material.module';
import { ShareModule } from '../share/share.module';

@NgModule({
  declarations: [GroupListComponent],
  imports: [
    CommonModule,
    ManagerRoutingModule,
    MaterialModule,
    ShareModule
  ]
})
export class ManagerModule { }
