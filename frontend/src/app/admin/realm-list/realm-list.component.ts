import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { RealmFragment, RealmsGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-realm-list',
  templateUrl: './realm-list.component.html',
  styleUrls: ['./realm-list.component.scss']
})
export class RealmListComponent implements OnInit {
  dataSource = new MatTableDataSource<RealmFragment>();
  isLoading = false;

  constructor(private realmsGQL: RealmsGQL) { }

  ngOnInit(): void {
    this.onLoad()
  }

  @ViewChild(MatPaginator) paginator?: MatPaginator = undefined;

  ngAfterViewInit() {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  onLoad() {
    if (this.isLoading) {
      return
    }
    this.isLoading = true
    this.dataSource.data = []
    this.realmsGQL.fetch()
      .subscribe(
        res => {
          this.dataSource.data = res.data.realms ?? []
        },
        () => this.isLoading = false,
        () => this.isLoading = false);
  }
}
