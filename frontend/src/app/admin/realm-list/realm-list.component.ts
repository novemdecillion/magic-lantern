import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { format, parseISO } from 'date-fns';
import { Observable } from 'rxjs';
import { concatMap, map, share, tap } from 'rxjs/operators';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { RealmFragment, RealmsGQL, SyncRealmGQL } from 'src/generated/graphql';
import { SYSTAEM_REALM_ID } from 'src/app/constants';
import { errorMessageIfNeed } from 'src/app/utilities';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-realm-list',
  templateUrl: './realm-list.component.html'
})
export class RealmListComponent implements OnInit {
  dataLoad: Observable<RealmFragment[]> | null = null;

  constructor(private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private realmsGql: RealmsGQL, private syncRealmGql: SyncRealmGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.fetch()
  }

  fetch(): Observable<RealmFragment[]> {
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

  canSync(realmId: string) {
    return realmId !== SYSTAEM_REALM_ID;
  }

  onSync(realmId: string) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '手動同期', message: `認証サーバとユーザ情報の同期を行います。この処理が完了するまで数分かかります。よろしですか?` } })
      .afterClosed().subscribe(isOk => {
        if (isOk) {
          this.dataLoad = this.dataLoad = this.syncRealmGql.mutate({realmId})
            .pipe(
              tap(res => {
                errorMessageIfNeed(res, this.snackBar)
              }),
              concatMap(_ => this.fetch()))
        }
      });
  }
}
