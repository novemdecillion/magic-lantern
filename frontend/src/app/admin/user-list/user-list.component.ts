import { Component, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { ColumnDefinition } from 'src/app/share/list-page/list-page.component';
import { UserFragment, UsersGQL } from 'src/generated/graphql';
import { ComponentStore } from '@ngrx/component-store';
import { map, share } from 'rxjs/operators';
import { PageService } from 'src/app/share/page/page.service';

interface UserRecord extends UserFragment {
  realmName?: string
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  providers: [ComponentStore]
})
export class UserListComponent implements OnInit/*, OnDestroy*/ {
  // loadDataSubscription: Subscription;
  dataLoad: Observable<UserRecord[]> | null = null;

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

  constructor(private usersGql: UsersGQL/*, pageService: PageService*/) {
    // this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  // ngOnDestroy(): void {
  //   this.loadDataSubscription.unsubscribe();
  // }

  onLoadData() {
    // this.isLoading = true;
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

  // dataForFilter(data: UserRecord): string {
  //   return `${data.userName} ${data.realmName} ${this.enableForDisplay(data.enabled)}`
  // }
}
