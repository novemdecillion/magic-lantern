import { Component, OnInit } from '@angular/core';
import { UserFragment, UsersGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  users: UserFragment[] = [];

  constructor(private usersGql: UsersGQL) { }

  ngOnInit(): void {
    this.usersGql.fetch().subscribe(res => {
      this.users = res.data.users ?? []
    })
  }

}
