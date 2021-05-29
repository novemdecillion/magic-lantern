import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AddGroupGQL, UpdateGroupGQL, DeleteGroupGQL } from 'src/generated/graphql';
import { GroupNode } from '../../utilities';

export interface EditGroupCommand extends GroupNode {
  // group: GroupNode;
  type: 'new' | 'edit' | 'delete';
}

@Component({
  selector: 'app-edit-group',
  templateUrl: './edit-group.component.html'
})
export class EditGroupComponent implements OnInit {
  title: string;
  groupNode: GroupNode;

  constructor(
      private dialogRef: MatDialogRef<EditGroupComponent>,
      @Inject(MAT_DIALOG_DATA) public command: EditGroupCommand,
      private addGroupGql: AddGroupGQL,
      private editGroupGql: UpdateGroupGQL,
      private deleteGroupGql: DeleteGroupGQL) {
    switch (command.type) {
      case 'new':
        this.title = '子グループ作成'
        this.groupNode = {
          groupId: '',
          groupName: '',
          groupGenerationId: command.groupGenerationId,
          parentGroupId: command.groupId,
          parentGroupName: command.groupName,
          path: []
        }
        break;
      case 'edit':
        this.title = 'グループ編集'
        this.groupNode = Object.assign({}, command);
        break;
      case 'delete':
        this.title = 'グループ削除'
        this.groupNode = Object.assign({}, command);
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
              currentGenerationId: this.groupNode.groupGenerationId,
              groupName: this.groupNode.groupName,
              parentGroupId: this.groupNode.parentGroupId!
            }
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
      case 'edit':
        this.editGroupGql
          .mutate({
            command: {
              groupId: this.groupNode.groupId,
              currentGenerationId: this.groupNode.groupGenerationId,
              groupName: this.groupNode.groupName,
              parentGroupId: this.groupNode.parentGroupId!
            }
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
      case 'delete':
        this.deleteGroupGql
          .mutate({
            command: {
              groupId: this.groupNode.groupId,
              currentGenerationId: this.groupNode.groupGenerationId
            }
          })
          .subscribe(_ => this.dialogRef.close(true));
        break;
    }
  }

}
