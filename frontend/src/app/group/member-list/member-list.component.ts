import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { PageService } from 'src/app/share/page/page.service';
import { GroupAppendableMembersGQL, GroupMembersGQL, UserFragment } from 'src/generated/graphql';
import { AddMemberComponent } from '../add-member/add-member.component';
import { EditMemberComponent } from '../edit-member/edit-member.component';
import { createGroupName, createRoleName } from '../../utilities';


export interface MemberRecord extends UserFragment {
  roleName?: string
}

@Component({
  selector: 'app-member-list',
  templateUrl: './member-list.component.html',
  styles: [
  ]
})
export class MemberListComponent implements OnInit {
  dataLoad: Observable<MemberRecord[]> | null = null;
  groupId: string;

  columns: ColumnDefinition<MemberRecord>[] = [
    {
      name: 'userName',
      headerName: '氏名'
    },
    {
      name: 'roleName',
      headerName: '権限'
    },
    // {
    //   name: 'enabled',
    //   displayName: '有効'
    // }
  ];

  groupName: string = ''
  isTopManageableGroup = false;

  loadDataSubscription: Subscription;

  constructor(
      private groupMembersGql: GroupMembersGQL,
      pageService: PageService,
      route: ActivatedRoute,
      private router: Router,
      public dialog: MatDialog) {
    this.groupId = route.snapshot.paramMap.get('id')!;
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
  }

  onLoadData(): void {
    this.dataLoad = this.groupMembersGql.fetch({groupId: this.groupId})
      .pipe(
        map(res => {
          if (res.data.group) {
            this.groupName = createGroupName(res.data.group.group.groupName, res.data.group.path);
          } else {
            // グループが存在しない。
            this.router.navigateByUrl('/group/list');
          }
          this.isTopManageableGroup = res.data.isTopManageableGroup;
          return res.data.groupMembers.map((member: MemberRecord) => {
            member.roleName = createRoleName(member.authorities[0].roles);
            return member;
          })
        }),
        share()
      );
  }

  canAddDeleteMember(): boolean {
    return !this.isTopManageableGroup;
  }

  onAddMember() {
    this.dialog.open(AddMemberComponent, { data: this.groupId })
    .afterClosed().subscribe(res => {
      if (res) {
        this.onLoadData();
      }
    });
  }

  onDeleteMember() {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { type: 'delete', members}})
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onEditMember() {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { type: 'edit', members}})
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

}
