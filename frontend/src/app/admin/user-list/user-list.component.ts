import { Component, OnInit, ViewChild } from '@angular/core';
import { Maybe, UserFragment, UsersGQL } from 'src/generated/graphql';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';

interface UserRecord extends UserFragment {
  realmName?: string
}

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  dataSource = new MatTableDataSource<UserRecord>();
  isLoading = false;

  constructor(private usersGql: UsersGQL) { }

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
    this.usersGql.fetch()
      .subscribe(
        res => {
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
            })

          this.dataSource.data = records
        },
        () => this.isLoading = false,
        () => this.isLoading = false);
  }
}
