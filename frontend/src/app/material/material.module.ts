import { NgModule } from "@angular/core";
import { MatButtonModule } from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';

import { MatAutoHeaderRowDef, MatAutoRowDef } from "./table";

const DECLARATIONS = [
  MatAutoHeaderRowDef,
  MatAutoRowDef
];

const MODULES = [
  MatButtonModule,
  MatCardModule,
  MatIconModule,
  MatListModule,
  MatMenuModule,
  MatSidenavModule,
  MatToolbarModule,
  MatTableModule
];

@NgModule({
  declarations: [
    ...DECLARATIONS
  ],
  imports: [
    ...MODULES
  ],
  exports: [
    ...DECLARATIONS,
    ...MODULES
  ]
})
export class MaterialModule { }
