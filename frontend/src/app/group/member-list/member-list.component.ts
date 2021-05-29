import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ForMemberListGQL, UserFragment } from 'src/generated/graphql';
import { EditMemberCommand, EditMemberComponent } from '../edit-member/edit-member.component';
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
  groupName: string = ''
  isTopManageableGroup = false;

  constructor(
      private forMemberListPageGql: ForMemberListGQL,
      route: ActivatedRoute,
      private router: Router,
      public dialog: MatDialog) {
    this.groupId = route.snapshot.paramMap.get('id')!;
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData(): void {
    this.dataLoad = this.forMemberListPageGql.fetch({groupId: this.groupId})
      .pipe(
        map(res => {
          if (res.data.group) {
            this.groupName = createGroupName(res.data.group.groupName, res.data.group.path);
          } else {
            // グループが存在しない。
            this.router.navigateByUrl('/group/list');
          }
          this.isTopManageableGroup = res.data.isTopManageableGroupByUser;
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
    this.dataLoad!.subscribe(members => {
      this.dialog.open<EditMemberComponent, EditMemberCommand, boolean>(
        EditMemberComponent,
        { data: { groupId: this.groupId,  type: 'add'}})
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onDeleteMember() {
    this.dataLoad!.subscribe(members => {
      this.dialog.open<EditMemberComponent, EditMemberCommand, boolean>(
        EditMemberComponent,
        { data: { groupId: this.groupId, type: 'delete' } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onEditMember() {
    this.dataLoad!.subscribe(members => {
      this.dialog.open<EditMemberComponent, EditMemberCommand, boolean>(
        EditMemberComponent,
        { data: { groupId: this.groupId, type: 'update'} })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

}
