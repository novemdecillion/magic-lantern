import { Component, OnInit } from '@angular/core';
import { Observable, merge } from 'rxjs';
import { BUILT_IN_ADMIN_USER, DEFAULT_GROUP_ID } from 'src/app/constants';
import { DeleteUserGQL, Role, UserFragment, UsersGQL } from 'src/generated/graphql';
import { map, share, withLatestFrom } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { EditUserComponent } from '../edit-user/edit-user.component';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { select, Store } from '@ngrx/store';
import { State, getUser } from 'src/app/root/store';
import { errorMessageIfNeed } from 'src/app/utilities';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface UserRecord extends UserFragment {
  isAdmin: boolean;
  isGroup: boolean;
  isSlide: boolean;
  isCurrentUser: boolean;
  realmName: string;
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit {
  dataLoad: Observable<UserRecord[]> | null = null;

  currentUserId$ = this.store.pipe(select(getUser), map(user => user?.userId));

  constructor(private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private store: Store<State>,
    private usersGql: UsersGQL,
    private deleteUserGql: DeleteUserGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.usersGql.fetch()
      .pipe(
        withLatestFrom(this.currentUserId$),
        map(([res, currentUserId]) => {
          let realmToName: { [key: string]: string } = {};
          (res.data.realms ?? [])
            .forEach(realm => {
              if (realm.realmName) {
                realmToName[realm.realmId] = realm.realmName;
              }
            });

          return (res.data.users ?? [])
            .map((user) => {
              let defaultGroupRoles = user.authorities.find(auth => auth.groupId == DEFAULT_GROUP_ID)?.roles ?? [];
              return Object.assign(user, {
                isAdmin: defaultGroupRoles?.includes(Role.Admin),
                isGroup: defaultGroupRoles?.includes(Role.Group),
                isSlide: defaultGroupRoles?.includes(Role.Slide),
                isCurrentUser: user.userId === currentUserId,
                realmName: realmToName[user.realmId]
              });
            });
        }),
        share()
      );
  }

  onAddUser() {
    this.dialog.open(EditUserComponent, { width: '500px'})
      .afterClosed()
      .subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onEditUser(row: UserRecord) {
    this.dialog.open(EditUserComponent, { data: row , width: '500px'})
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  canDelete(row: UserRecord): boolean {
    return !row.isCurrentUser && (row.isSystemRealm === true) && (row.userId !== BUILT_IN_ADMIN_USER)
  }

  onDeleteUser(row: UserRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: 'ユーザ削除', message: `「${row.userName}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(isOk => {
        if (isOk) {
          this.deleteUserGql.mutate({ userId: row.userId })
          .subscribe(res => {
            errorMessageIfNeed(res, this.snackBar)
            this.onLoadData()
          });
      }
      });
  }

}
