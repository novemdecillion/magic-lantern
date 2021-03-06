import { Component, ContentChildren, Directive, Input, OnInit, TemplateRef, ViewChild, AfterContentInit, ViewEncapsulation, ContentChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { format } from 'date-fns';
import { Observable } from 'rxjs';


@Directive({
  selector: '[listCell]',
})
export class ListCellDirective {
  constructor(public template: TemplateRef<any>) { }
}

@Directive({
  selector: '[listHeaderCell]',
})
export class ListHeaderCellDirective {
  constructor(public template: TemplateRef<any>) { }
}

export interface ColumnDefinition<Record> {
  name: string;
  valueFrom?: (column: ColumnDefinition<Record>, row: Record)=>string;
  headerName: string | null;
  sort?: boolean;
  cellTemplate?: TemplateRef<any>;
  headerCellTemplate?: TemplateRef<any>;
}

@Directive({
  selector: 'app-list-column',
})
export class ColumnDefinitionDirective<Record> implements ColumnDefinition<Record> {
  @Input()
  name!: string;
  @Input()
  valueFrom?: (column: ColumnDefinition<Record>, row: Record)=>string;
  @Input()
  headerName: string | null = null;
  @Input()
  sticky?: 'start' | 'end';
  @Input()
  sort?: boolean;

  @ContentChild(ListCellDirective)
  cell: ListCellDirective | null = null;

  @ContentChild(ListHeaderCellDirective)
  headerCell: ListCellDirective | null = null;

  constructor() { }
}

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ListComponent<Record extends {[key: string]: any}> implements OnInit, AfterContentInit {
  @Input() enableFilter: boolean = true;
  @Input() columns: ColumnDefinition<Record>[] = [];

  @Input() pageSize=20;
  @Input() pageSizeOptions=[20, 50, 100];
  @Input() hidePageSize=false;

  @Input()
  set dataLoad(observable: Observable<Record[]> | null) {
    if (observable) {
      observable.subscribe(records => this.dataSource.data = records)
    } else {
      this.dataSource.data = [];
    }
  }

  dataSource = new MatTableDataSource<Record>();

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort!: MatSort;
  @ContentChildren(ColumnDefinitionDirective) columnDefs: ColumnDefinitionDirective<Record>[] | null = null

  displayedColumns: string[] = [];

  constructor() {
    this.dataSource.filterPredicate = (data: Record, filter: string) => {
      let dataStr = this.columns.reduce<string>((accumulator, column) => {
        let val = this.defaultDisplayValue(column, data)
        if ((val === undefined) || (val === null)) {
          return accumulator;
        }
        return (0 < accumulator.length)? `${accumulator} ${val}`: val.toString();
      }, '')
      return filter.trim().split(' ').some(filterWord => dataStr.indexOf(filterWord) != -1);
    }
  }

  ngOnInit(): void {
  }

  ngAfterContentInit(): void {
    if (this.columnDefs) {
      this.columns = this.columnDefs
    }
    this.displayedColumns = this.columns.map(column => column.name)
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  defaultDisplayValue(column: ColumnDefinition<Record>, row: Record): string {
    if (column.valueFrom) {
      return column.valueFrom(column, row);
    }

    let val = row[column.name]

    switch (Object.prototype.toString.call(val)) {
      case '[object Boolean]':
        return val ? '○' : '×';
      case '[object Date]':
        return format(val, 'yyyy-MM-dd HH:mm');
    }
    return val
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue;
  }
}
