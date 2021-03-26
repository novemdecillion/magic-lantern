import { BooleanInput } from "@angular/cdk/coercion";
import { CdkFooterRowDef, CdkHeaderRowDef, CdkRowDef } from "@angular/cdk/table";
import { Directive } from "@angular/core";
import { MatTable } from "@angular/material/table";

@Directive({
  selector: '[matAutoHeaderRowDef]',
  providers: [{provide: CdkHeaderRowDef, useExisting: MatAutoHeaderRowDef}],
  inputs: ['columns: matAutoHeaderRowDef', 'sticky: matHeaderRowDefSticky'],
})
export class MatAutoHeaderRowDef extends CdkHeaderRowDef {
  static ngAcceptInputType_sticky: BooleanInput;
  ngAfterContentInit() {
    this.columns = (this._table as MatTable<any>)._contentColumnDefs
    .map( def => def.name )
    this._columnsDiffer = this._differs.find(this.columns).create();
    this._columnsDiffer.diff(this.columns);
  }
}

@Directive({
  selector: '[matAutoFooterRowDef]',
  providers: [{provide: CdkFooterRowDef, useExisting: MatAutoFooterRowDef}],
  inputs: ['columns: matAutoFooterRowDef', 'sticky: matFooterRowDefSticky'],
})
export class MatAutoFooterRowDef extends CdkFooterRowDef {
  static ngAcceptInputType_sticky: BooleanInput;
  ngAfterContentInit() {
    this.columns = (this._table as MatTable<any>)._contentColumnDefs
    .map( def => def.name )
    this._columnsDiffer = this._differs.find(this.columns).create();
    this._columnsDiffer.diff(this.columns);
  }
}

@Directive({
  selector: '[matAutoRowDef]',
  providers: [{provide: CdkRowDef, useExisting: MatAutoRowDef}],
  inputs: ['columns: matAutoRowDefColumns', 'when: matRowDefWhen'],
})
export class MatAutoRowDef<T> extends CdkRowDef<T> {
  ngAfterContentInit() {
    this.columns = (this._table as MatTable<any>)._contentColumnDefs
      .map( def => def.name );
    this._columnsDiffer = this._differs.find(this.columns).create();
    this._columnsDiffer.diff(this.columns);
  }
}
