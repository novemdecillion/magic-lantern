import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserRecord } from '../user-list/user-list.component';
import { AddUserGQL, UpdateUserGQL, AddUserCommand, UpdateUserCommand } from 'src/generated/graphql';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SYSTAEM_REALM_NAME, BUILT_IN_ADMIN_USER } from 'src/app/constants';
import { errorMessageIfNeed } from 'src/app/utilities';
import { Store } from '@ngrx/store';
import { State, AppActions } from 'src/app/root/store';

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
    private store: Store<State>,
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

  isBuiltInAdminUser(): boolean {
    return this.user.userId == BUILT_IN_ADMIN_USER;
  }

  copyToCommand(command: AddUserCommand | UpdateUserCommand) {
    command.accountName = this.user.accountName
    command.userName = this.user.userName
    command.password = this.password
    command.email = this.user.email
    command.enabled = this.user.enabled
    command.isAdmin = this.user.isAdmin
    command.isGroup = this.user.isGroup
    command.isSlide = this.user.isSlide
  }

  onOK(): void {
    if (this.isEdit) {
      let command: UpdateUserCommand = <any>{};
      this.copyToCommand(command);
      command.userId = this.user.userId
      this.updateUserGql
        .mutate({ command })
        .subscribe(res =>  {
          if (!errorMessageIfNeed(res, this.snackBar)) {
            if (this.user.isCurrentUser) {
              this.store.dispatch(AppActions.loadCurrentUser());
            }

            this.dialogRef.close(true);
          }
        })
    } else {
      let command: AddUserCommand = <any>{};
      this.copyToCommand(command);
      this.addUserGql
        .mutate({ command })
        .subscribe(res => {
          if (!errorMessageIfNeed(res, this.snackBar)) {
            this.dialogRef.close(true);
          }
        });
    }
  }

}
