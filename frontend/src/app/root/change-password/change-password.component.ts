import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { errorCode, errorMessageIfNeed, logout } from 'src/app/utilities';
import { ChangePasswordCommand, ChangePasswordGQL, UserApiResult } from 'src/generated/graphql';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styles: [
  ]
})
export class ChangePasswordComponent {
  command: ChangePasswordCommand = <any>{};
  confirmNewPassword!: string;

  constructor(private dialogRef: MatDialogRef<ChangePasswordComponent>,
    private snackBar: MatSnackBar,
    private changePasswordGql: ChangePasswordGQL) { }

  onOK(): void {
    this.changePasswordGql.mutate({ command: this.command })
      .subscribe(res => {
        if (errorCode(res).includes(UserApiResult.UserNotFound)) {
          logout();
        }

        if (!errorMessageIfNeed(res, this.snackBar, !res.data?.changePassword)) {
          this.dialogRef.close(true)
        }
      });
  }

}
