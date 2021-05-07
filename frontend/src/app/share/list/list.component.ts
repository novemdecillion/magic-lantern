import { Component, Input, OnInit, TemplateRef, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';

export interface ColumnDefinition<Record> {
  name: string;
  valueFrom?: (column: ColumnDefinition<Record>, row: Record)=>string;
  headerName: string | null;
  sticky?: 'start' | 'end';
  sort?: boolean;
  cellTemplate?: TemplateRef<any>;
  headerCellTemplate?: TemplateRef<any>;
}

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ListComponent<Record extends {[key: string]: any}> implements OnInit {
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

  displayedColumns: string[] = [];

  constructor() {
    this.dataSource.filterPredicate = (data: Record, filter: string) => {
      let dataStr = this.columns.reduce<string>((accumulator, column) => {
        let val = this.defaultDisplayValue(column, data)
        return (0 < accumulator.length)? `${accumulator} ${val}`: val
      }, '')
      return filter.trim().split(' ').some(filterWord => dataStr.indexOf(filterWord) != -1);
    }
  }

  ngOnInit(): void {
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
        return val ? '○' : '×'
    }
    return val
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue;
  }
}
