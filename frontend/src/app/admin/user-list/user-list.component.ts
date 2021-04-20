import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ColumnDefinition } from 'src/app/share/list-page/list-page.component';
import { UserFragment, UsersGQL } from 'src/generated/graphql';

interface UserRecord extends UserFragment {
  realmName?: string
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent {
  columns: ColumnDefinition<UserRecord>[] = [
    {
      name: 'userName',
      displayName: '氏名'
    },
    {
      name: 'realmName',
      displayName: '認証サーバ'
    },
    {
      name: 'enabled',
      displayName: '有効'
    }
  ];

  constructor(private usersGql: UsersGQL) {
  }

  dataLoader = (): Observable<UserRecord[]> => {
    return this.usersGql.fetch()
      .pipe(map(res => {
          let realmToName: { [key: string]: string } = {};
          (res.data.realms ?? [])
            .forEach(realm => {
              if (realm.realmName) {
                realmToName[realm.realmId] = realm.realmName;
              }
            });

          let records = (res.data.users ?? [])
            .map((user: UserRecord) => {
              user.realmName = 'システム'
              if (user.realmId) {
                user.realmName = realmToName[user.realmId]
              }
              return user
            });

          return records;
        }));
  }

  // dataForFilter(data: UserRecord): string {
  //   return `${data.userName} ${data.realmName} ${this.enableForDisplay(data.enabled)}`
  // }
}
