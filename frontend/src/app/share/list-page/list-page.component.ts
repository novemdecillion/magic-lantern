import { Component, Input, OnInit, AfterViewInit, TemplateRef, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable, EMPTY } from 'rxjs';
import { finalize } from 'rxjs/operators';

export interface ColumnDefinition<Record> {
  name: string;
  displayName?: string | null;
  sticky?: 'start' | 'end';
  sort?: boolean;
  displayValue?: (column: ColumnDefinition<Record>, row: Record)=>string;
  cellTemplate?: TemplateRef<any>;
}

@Component({
  selector: 'app-list-page',
  templateUrl: './list-page.component.html'
})
export class ListPageComponent<Record extends {[key: string]: any}> implements OnInit {
  @Input() titleTemplateRef: TemplateRef<any> | null = null;
  @Input() enableFilter: boolean = true;
  @Input() columns: ColumnDefinition<Record>[] = [];
  @Input() dataLoader: ()=>Observable<Record[]> = this.defaultDataLoader

  dataSource = new MatTableDataSource<Record>();

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  displayedColumns: string[] = [];
  isLoading = false;

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
    this.onLoadData();
    this.displayedColumns = this.columns.map(column => column.name)
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onLoadData() {
    this.dataSource.data = [];
    this.isLoading = true
    this.dataLoader()
      .pipe(finalize(() => this.isLoading = false))
      .subscribe(records => {
        this.dataSource.data = records
      });
  }

  defaultDataLoader(): Observable<Record[]> {
    return EMPTY;
  }

  defaultDisplayValue(column: ColumnDefinition<Record>, row: Record): string {
    if (column.displayValue) {
      return column.displayValue(column, row);
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
