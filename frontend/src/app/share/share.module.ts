import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListPageComponent } from './list-page/list-page.component';
import { MaterialModule } from '../material/material.module';
import { PageComponent } from './page/page.component';



@NgModule({
  declarations: [ListPageComponent, PageComponent],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    ListPageComponent
  ]
})
export class ShareModule { }
