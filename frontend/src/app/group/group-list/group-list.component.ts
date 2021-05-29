import { Component, OnInit } from '@angular/core';
import { EffectiveGroupsGQL, GroupWithMemberCountFragment, Role } from 'src/generated/graphql';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { EditGroupComponent } from '../edit-group/edit-group.component';
import { DEFAULT_GROUP_ID } from 'src/app/constants'
import { createGroupNodes, IGroupNode } from '../../utilities';
import { EditMemberCommand, EditMemberComponent } from '../edit-member/edit-member.component';

type GroupNodeWithMemberCount = IGroupNode & GroupWithMemberCountFragment

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit {
  dataLoad: Observable<GroupNodeWithMemberCount[]> | null = null;

  constructor(private groupsGql: EffectiveGroupsGQL, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData = (): void => {
    this.dataLoad = this.groupsGql.fetch({role: Role.Group})
      .pipe(
        map(res => {
          let [nodes, _] = createGroupNodes(res.data.effectiveGroupsByUser)
          return Object.values(nodes);
        }),
        share()
      );
  }

  onNewChildGroup(group: GroupNodeWithMemberCount): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'new' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onEditGroup(group: GroupNodeWithMemberCount): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'edit' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  isEntireGroup(group: GroupNodeWithMemberCount): boolean {
    return group.groupId === DEFAULT_GROUP_ID
  }

  onDeleteGroup(group: GroupNodeWithMemberCount): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'delete' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onAddMember(group: GroupNodeWithMemberCount): void {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { groupId: group.groupId, type: 'add'} as EditMemberCommand })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onEditMember(group: GroupNodeWithMemberCount): void {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { groupId: group.groupId, type: 'update'} as EditMemberCommand })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onDeleteMember(group: GroupNodeWithMemberCount): void {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { groupId: group.groupId, type: 'delete'} as EditMemberCommand })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }
}
