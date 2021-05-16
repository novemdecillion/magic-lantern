import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { format, parseISO } from 'date-fns';
import { Observable } from 'rxjs';
import { concatMap, map, share } from 'rxjs/operators';
import { RealmFragment, RealmsGQL, SyncRealmGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-realm-list',
  templateUrl: './realm-list.component.html'
})
export class RealmListComponent implements OnInit {
  dataLoad: Observable<RealmFragment[]> | null = null;

  constructor(private realmsGql: RealmsGQL, private syncRealmGql: SyncRealmGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

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
