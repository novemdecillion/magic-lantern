import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserFragment, UsersGQL } from 'src/generated/graphql';
import { map, share } from 'rxjs/operators';

interface UserRecord extends UserFragment {
  realmName?: string
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html'
})
export class UserListComponent implements OnInit {
  dataLoad: Observable<UserRecord[]> | null = null;

  constructor(private usersGql: UsersGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.usersGql.fetch()
      .pipe(
        map(res => {
          let realmToName: { [key: string]: string } = {};
          (res.data.realms ?? [])
            .forEach(realm => {
              if (realm.realmName) {
                realmToName[realm.realmId] = realm.realmName;
              }
            });

          return (res.data.users ?? [])
            .map((user: UserRecord) => {
              user.realmName = 'システム'
              if (user.realmId) {
                user.realmName = realmToName[user.realmId]
              }
              return user
            });
        }),
        share()
      );
  }
}
