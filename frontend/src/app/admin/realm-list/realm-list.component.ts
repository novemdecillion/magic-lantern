import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { format, parseISO } from 'date-fns';
import { from, Observable, Subscription } from 'rxjs';
import { concatMap, finalize, map, publish, share } from 'rxjs/operators';
import { ColumnDefinition, ListPageComponent } from 'src/app/share/list-page/list-page.component';
import { PageService } from 'src/app/share/page/page.service';
import { RealmFragment, RealmsGQL, SyncRealmGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-realm-list',
  templateUrl: './realm-list.component.html'
})
export class RealmListComponent implements OnInit/*, OnDestroy*/ {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  // loadDataSubscription: Subscription;
  dataLoad: Observable<RealmFragment[]> | null = null;

  columns: ColumnDefinition<RealmFragment>[] = [
  ];

  constructor(private realmsGql: RealmsGQL, private syncRealmGql: SyncRealmGQL/*, pageService: PageService*/) {
    // this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
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
    this.onLoadData();
  }

  // ngOnDestroy(): void {
  //   this.loadDataSubscription.unsubscribe();
  // }

  onLoadData() {
    this.dataLoad = this.fetch();
  }

  fetch() {
    return this.realmsGql.fetch()
      .pipe(
        map(res => {
          res.data.realms?.forEach(realm => {
            if (realm.syncAt) {
              realm.syncAt = format(parseISO(realm.syncAt), 'yyyy/MM/dd HH:mm:ss')
            }
          })
          return res.data.realms
        }),
        share());
  }

  onSync(realmId?: string) {
    this.dataLoad = this.syncRealmGql.mutate({realmId})
      .pipe(concatMap(_ => this.fetch()))
  }
}
