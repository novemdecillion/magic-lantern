import { Injectable, NgModule } from "@angular/core";
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatDialogModule, MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
// import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
// import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from "@angular/material/sort";
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatTreeModule } from '@angular/material/tree';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatAutoHeaderRowDef, MatAutoRowDef } from "./table";
import { DateAdapter, MatNativeDateModule, MAT_DATE_LOCALE, NativeDateAdapter } from "@angular/material/core";
import { FlexLayoutModule } from '@angular/flex-layout';

const DECLARATIONS = [
  MatAutoHeaderRowDef,
  MatAutoRowDef
];

const MODULES = [
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatDialogModule,
  MatExpansionModule,
  // MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  // MatProgressSpinnerModule,
  MatRadioModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTreeModule,
  MatToolbarModule,
  MatTooltipModule,

  FlexLayoutModule
];

@Injectable()
class JaDateAdapter extends NativeDateAdapter {
  getDateNames(): string[] {
    const dateNames: string[] = [];
    for (let i = 0; i < 31; i++) {
      dateNames[i] = String(i + 1);
    }
    return dateNames;
  }
}

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
  ],
  providers: [
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { minWidth: 320 }},
    // {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 5000}},
    {provide: MAT_DATE_LOCALE, useValue: 'ja-JP'},
    {provide: DateAdapter, useClass: JaDateAdapter}
  ]
})
export class MaterialModule { }
