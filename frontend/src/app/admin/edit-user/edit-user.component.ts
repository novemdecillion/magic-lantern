import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserRecord } from '../user-list/user-list.component';
import { AddUserGQL, UpdateUserGQL, AddUserResult, UpdateUserResult, AddUserCommand, UpdateUserCommand } from 'src/generated/graphql';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SYSTAEM_REALM_NAME } from 'src/app/constants';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styles: [
  ]
})
export class EditUserComponent {
  isEdit: boolean = false;
  isEditSystemRealmUser: boolean = false;
  isEditOuterRealmUser: boolean = false;

  user: UserRecord;
  password: string | null = null;
  confirmPassword: string | null = null;

  isEditPassword: boolean;

  constructor(private dialogRef: MatDialogRef<EditUserComponent>,
    @Inject(MAT_DIALOG_DATA) user: UserRecord,
    private snackBar: MatSnackBar,
    private addUserGql: AddUserGQL, private updateUserGql: UpdateUserGQL) {

    if (user) {
      this.isEdit = true;
      if (user.isSystemRealm) {
        this.isEditSystemRealmUser = true;
      } else {
        this.isEditOuterRealmUser = true;
      }

      this.isEditPassword = false
      this.user = Object.assign({}, user);
      delete this.user.__typename;
    } else {
      this.isEditPassword = true
      this.user = <any>{
        realmName: SYSTAEM_REALM_NAME,
        enabled: true
      }
    }
  }

  copyToCommand(command: AddUserCommand | UpdateUserCommand) {
    command.accountName = this.user.accountName
    command.userName = this.user.userName
    command.password = this.password
    command.email = this.user.email
    command.enabled = this.user.enabled
    command.isAdmin = this.user.isAdmin
    command.isGroupManager = this.user.isGroupManager
  }

  onOK(): void {
    if (this.isEdit) {
      let command: UpdateUserCommand = <any>{};
      this.copyToCommand(command);
      command.userId = this.user.userId
      this.updateUserGql
        .mutate({ command })
        .subscribe(res =>  {
          switch (res.data?.updateUser) {
            case UpdateUserResult.Success:
              this.dialogRef.close(true)
              break;
            default:
              this.snackBar.open('ユーザの更新に失敗しました。', 'OK')
              break;
          }
        })
    } else {
      let command: AddUserCommand = <any>{};
      this.copyToCommand(command);
      this.addUserGql
        .mutate({ command })
        .subscribe(res => {
          switch (res.data?.addUser) {
            case AddUserResult.Success:
              this.dialogRef.close(true)
              break;
            case AddUserResult.DuplicateAccountName:
              this.snackBar.open('指定のアカウント名は既に存在しています。', 'OK')
              break;
            default:
              this.snackBar.open('ユーザの追加に失敗しました。', 'OK')
              break;
          }
        });
    }
  }

}
