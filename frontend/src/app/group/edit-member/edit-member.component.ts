import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { map, share, tap } from 'rxjs/operators';
import { DEFAULT_GROUP_ID } from 'src/app/constants';
import { ListComponent } from 'src/app/share/list/list.component';
import { RoleMap, createGroupName, createRoleName } from 'src/app/utilities';
import { AddGroupMemberGQL, UpdateGroupMemberGQL, DeleteGroupMemberGQL, ForAddMemberGQL, ForEditMemberGQL, GroupFragment, Role } from 'src/generated/graphql';
import { MemberRecord } from '../member-list/member-list.component';

type EditMemberRecord = MemberRecord & {
  selected?: boolean
}

export interface EditMemberCommand {
  groupId: string;
  type: 'add' | 'update' | 'delete';
}

@Component({
  selector: 'app-edit-member',
  templateUrl: './edit-member.component.html',
  styles: [
  ]
})
export class EditMemberComponent implements OnInit {
  @ViewChild(ListComponent, { static: true }) private list!: ListComponent<EditMemberRecord>;

  title: string

  dataLoad: Observable<EditMemberRecord[]> | null = null;

  roles: RoleMap<boolean> = {
    ADMIN: false,
    GROUP: false,
    SLIDE: false,
    LESSON: false,
    STUDY: false,
    NONE: false
  };
  constructor(
    private dialogRef: MatDialogRef<EditMemberComponent>,
    @Inject(MAT_DIALOG_DATA) public command: EditMemberCommand,
    private forAddMemberGql: ForAddMemberGQL,
    private forEditMemberGql: ForEditMemberGQL,
    private addGroupMemberGql: AddGroupMemberGQL,
    private updateGroupMemberGql: UpdateGroupMemberGQL,
    private deleteGroupMemberGql: DeleteGroupMemberGQL
  ) {
    switch (command.type) {
      case 'add':
        this.title = 'メンバー追加'
        break;
      case 'update':
        this.title = 'メンバー権限編集'
        break;
      case 'delete':
        this.title = 'メンバー削除'
        break;
    }
  }

  ngOnInit(): void {
    if (this.command.type == 'add') {
      this.dataLoad = this.forAddMemberGql
        .fetch({ groupId: this.command.groupId })
        .pipe(
          tap(res => {
            if (res.data.group) {
              this.updateTitle(res.data.group)
            }
          }),
          map(res => res.data.groupAppendableMembers),
          share()
        );
    } else {
      this.dataLoad = this.forEditMemberGql
        .fetch({ groupId: this.command.groupId })
        .pipe(
          tap(res => {
            if (res.data.group) {
              this.updateTitle(res.data.group)
            }
          }),
          map(res => {
            return res.data.groupMembers.map((member: MemberRecord) => {
              member.roleName = createRoleName(member.authorities[0].roles);
              return member;
            })
          }),
          share()
        );
    }
  }

  onOK() {
    let selectedMemberIds = this.list.dataSource.data.filter(member => member.selected).map(member => member.userId)

    let memberRoles = Object.entries(this.roles).filter(([_, value]) => value).map(([key, _]) => key as Role);
    if (0 === memberRoles.length) {
      memberRoles = [Role.None];
    }

    switch (this.command.type) {
      case 'add':
        this.addGroupMemberGql.mutate({command: {
          groupId: this.command.groupId,
          userIds: selectedMemberIds,
          role: memberRoles }})
        .subscribe(_ => this.dialogRef.close(true))
        break;
      case 'update':
        this.updateGroupMemberGql.mutate({command: {
          groupId: this.command.groupId,
          userIds: selectedMemberIds,
          role: memberRoles }})
        .subscribe(_ => this.dialogRef.close(true))
        break;
      case 'delete':
        this.deleteGroupMemberGql.mutate({command: {
          groupId: this.command.groupId,
          userIds: selectedMemberIds
        }})
        .subscribe(_ => this.dialogRef.close(true))
        break;
    }
  }

  updateTitle(group: GroupFragment) {
    this.title = `${createGroupName(group.groupName, group.path)} ${this.title}`
  }

  isDefaultGroup(): boolean {
    return this.command.groupId === DEFAULT_GROUP_ID;
  }

  get selectAll(): boolean | undefined {
    let selectedAll: boolean | undefined = undefined;
    this.list.dataSource.data.some((member, index)=> {
      let selected = member.selected ?? false
      if(0 == index) {
        selectedAll = selected
        return false
      } if (selectedAll != selected) {
        selectedAll = undefined;
        return true
      }
      return false
    })
    return selectedAll;
  }

  set selectAll(selected: boolean | undefined) {
    this.list.dataSource.data.forEach(member => member.selected = selected);
  }
}
