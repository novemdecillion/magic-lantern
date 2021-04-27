import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListPageComponent } from './list-page/list-page.component';
import { ListComponent } from './list/list.component';
import { MaterialModule } from '../material/material.module';
import { PageComponent } from './page/page.component';
import { FormsModule } from '@angular/forms';

const DECLARATIONS = [
  ListPageComponent, PageComponent, ListComponent
];

@NgModule({
  declarations: [...DECLARATIONS],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    ...DECLARATIONS,
    MaterialModule,
    FormsModule
  ]
})
export class ShareModule { }
