import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { map, share, tap, withLatestFrom } from 'rxjs/operators';
import { DEFAULT_GROUP_ID } from 'src/app/constants';
import { ListComponent } from 'src/app/share/list/list.component';
import { RoleMap, createGroupName, createRoleName, errorMessageIfNeed } from 'src/app/utilities';
import { AddGroupMemberGQL, UpdateGroupMemberGQL, DeleteGroupMemberGQL, ForAddMemberGQL, ForEditMemberGQL, GroupFragment, Role } from 'src/generated/graphql';
import { MemberRecord } from '../member-list/member-list.component';
import { select, Store } from '@ngrx/store';
import { State, AppActions, getUser } from 'src/app/root/store';
import { MatSnackBar } from '@angular/material/snack-bar';

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
  currentGenerationId!: number

  dataLoad: Observable<EditMemberRecord[]> | null = null;
  currentUserId$ = this.store.pipe(select(getUser), map(user => user?.userId));

  roles: RoleMap<boolean> = {
    ADMIN: false,
    GROUP: false,
    SLIDE: false,
    LESSON: false,
    STUDY: false
  };
  constructor(
    private dialogRef: MatDialogRef<EditMemberComponent>,
    @Inject(MAT_DIALOG_DATA) public command: EditMemberCommand,
    private snackBar: MatSnackBar,
    private store: Store<State>,
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
            this.currentGenerationId = res.data.currentGroupGenerationId
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
            this.currentGenerationId = res.data.currentGroupGenerationId
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

    switch (this.command.type) {
      case 'add':
        this.addGroupMemberGql.mutate({command: {
          currentGenerationId: this.currentGenerationId,
          groupId: this.command.groupId,
          userIds: selectedMemberIds,
          roles: memberRoles }})
          .subscribe(res => {
            if(!errorMessageIfNeed(res, this.snackBar)) {
              this.dialogRef.close(true)
            }
          })
        break;
      case 'update':
        this.updateGroupMemberGql.mutate({command: {
          currentGenerationId: this.currentGenerationId,
          groupId: this.command.groupId,
          userIds: selectedMemberIds,
          roles: memberRoles }})
        .pipe(withLatestFrom(this.currentUserId$))
        .subscribe(([res, currentUserId]) => {
          if (selectedMemberIds.includes(currentUserId!!)) {
            this.store.dispatch(AppActions.loadCurrentUser());
          }
          if(!errorMessageIfNeed(res, this.snackBar)) {
            this.dialogRef.close(true)
          }
        });
        break;
      case 'delete':
        this.deleteGroupMemberGql.mutate({command: {
          currentGenerationId: this.currentGenerationId,
          groupId: this.command.groupId,
          userIds: selectedMemberIds
        }})
        .subscribe(res => {
          if(!errorMessageIfNeed(res, this.snackBar)) {
            this.dialogRef.close(true)
          }
        })
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
