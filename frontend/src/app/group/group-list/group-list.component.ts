import { Component, OnInit } from '@angular/core';
import { EffectiveGroupsGQL, Role } from 'src/generated/graphql';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { EditGroupComponent } from '../edit-group/edit-group.component';
import { DEFAULT_GROUP_ID } from 'src/app/constants'
import { createGroupNodes, GroupNode } from '../../utilities';
import { EditMemberCommand, EditMemberComponent } from '../edit-member/edit-member.component';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit {
  dataLoad: Observable<GroupNode[]> | null = null;

  constructor(private groupsGql: EffectiveGroupsGQL, public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData = (): void => {
    this.dataLoad = this.groupsGql.fetch({role: Role.Group})
      .pipe(
        map(res => {
          let [nodes, _] = createGroupNodes(res.data.effectiveGroups)
          return Object.values(nodes);
        }),
        share()
      );
  }

  onNewChildGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'new' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onEditGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'edit' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  isEntireGroup(group: GroupNode): boolean {
    return group.groupId === DEFAULT_GROUP_ID
  }

  onDeleteGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'delete' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onAddMember(group: GroupNode): void {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { groupId: group.groupId, type: 'add'} as EditMemberCommand })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onEditMember(group: GroupNode): void {
    this.dataLoad!.subscribe(members => {
      this.dialog.open(EditMemberComponent, { data: { groupId: group.groupId, type: 'update'} as EditMemberCommand })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      })
    });
  }

  onDeleteMember(group: GroupNode): void {
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
