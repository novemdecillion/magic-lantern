import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColumnDefinitionDirective, ListCellDirective, ListComponent, ListHeaderCellDirective } from './list/list.component';
import { MaterialModule } from '../material/material.module';
import { PageComponent, PageTitleDirective, PageToolbarDirective } from './page/page.component';
import { FormsModule } from '@angular/forms';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';

const DECLARATIONS = [
  PageComponent,
  ListComponent, ColumnDefinitionDirective, ListCellDirective, ListHeaderCellDirective,
  ConfirmDialogComponent,
  PageTitleDirective, PageToolbarDirective
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
