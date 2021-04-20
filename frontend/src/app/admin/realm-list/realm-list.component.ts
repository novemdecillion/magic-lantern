import { Component, OnInit, TemplateRef, ViewChild, ViewEncapsulation } from '@angular/core';
// import { MatPaginator } from '@angular/material/paginator';
// import { MatSort } from '@angular/material/sort';
// import { MatTableDataSource } from '@angular/material/table';
import { format, parseISO } from 'date-fns';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
// import { AbstractListComponent } from 'src/app/abstract-list.component';
import { ColumnDefinition, ListPageComponent } from 'src/app/share/list-page/list-page.component';
import { RealmFragment, RealmsGQL, SyncRealmGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-realm-list',
  templateUrl: './realm-list.component.html',
  styleUrls: ['./realm-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RealmListComponent implements OnInit {
  @ViewChild(ListPageComponent, { static: true }) private pageComponent!: ListPageComponent<RealmFragment>;
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  columns: ColumnDefinition<RealmFragment>[] = [
  ];

  constructor(private realmsGql: RealmsGQL, private syncRealmGql: SyncRealmGQL) {
  }

  ngOnInit(): void {
    this.columns = [
      {
        name: 'realmName',
        displayName: '認証サーバ'
      },
      {
        name: 'enabled',
        displayName: '有効'
      },
      {
        name: 'syncAt',
        displayName: '最終同期日時'
      },
      {
        name: 'operation',
        displayName: null,
        sort: false,
        cellTemplate: this.operationTemplate
      }
    ];
  }

  dataLoader = (): Observable<RealmFragment[]> => {
    return this.realmsGql.fetch()
      .pipe(map(res => {
          res.data.realms?.map(realm => {
            if (realm.syncAt) {
              realm.syncAt = format(parseISO(realm.syncAt), 'yyyy/MM/dd HH:mm:ss')
            }
          })
          return res.data.realms ?? []
        }));
  }

  onSync(realmId?: string) {
    this.syncRealmGql.mutate({realmId})
    .pipe(finalize(() => this.pageComponent.onLoadData()))
    .subscribe();
  }
}
