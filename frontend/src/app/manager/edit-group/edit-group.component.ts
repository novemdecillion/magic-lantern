import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddGroupGQL, EditGroupGQL, DeleteGroupGQL } from 'src/generated/graphql';
import { GroupRecord } from '../group-list/group-list.component';

export interface EditGroupCommand {
  group: GroupRecord;
  type: 'new' | 'edit' | 'delete';
}

@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html'
})
export class EditGroupComponent implements OnInit {
  title: string;
  group: GroupRecord;

  constructor(public dialogRef: MatDialogRef<EditGroupComponent>,
    @Inject(MAT_DIALOG_DATA) public command: EditGroupCommand,
    private addGroupGql: AddGroupGQL,
    private editGroupGql: EditGroupGQL,
    private deleteGroupGql: DeleteGroupGQL) {
    switch (command.type) {
      case 'new':
        this.title = '子グループ作成'
        this.group = {
          groupId: '',
          groupOriginId: '',
          groupName: '',
          groupGenerationId: command.group.groupGenerationId,
          parentGroupId: command.group.groupId,
          parentGroupName: command.group.groupName
        }
        break;
      case 'edit':
        this.title = 'グループ編集'
        this.group = Object.assign({}, command.group);
        break;
      case 'delete':
        this.title = 'グループ削除'
        this.group = Object.assign({}, command.group);
        break;
    }
  }

  ngOnInit(): void {
  }

  onOK() {
    switch (this.command.type) {
      case 'new':
        this.addGroupGql
          .mutate({
            command: {
              groupName: this.group.groupName,
              parentGroupId: this.group.parentGroupId!
            }
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
      case 'edit':
        this.editGroupGql
          .mutate({
            command: this.group
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
      case 'delete':
        this.deleteGroupGql
          .mutate({
            groupId: this.group.groupId
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
    }
  }

}
