import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { logout } from 'src/app/utilities';
import { ChangePasswordCommand, ChangePasswordGQL, ChangePasswordResult } from 'src/generated/graphql';

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
        switch (res.data?.changePassword) {
          case ChangePasswordResult.Success:
            this.dialogRef.close(true)
            break;
          case ChangePasswordResult.UserNotFound:
            logout();
            break;
          case ChangePasswordResult.PasswordNotMatch:
            this.snackBar.open('現在のパスワードが誤っています。', 'OK')
            break;
          default:
            this.snackBar.open('パスワードの変更に失敗しました。', 'OK')
            break;
        }

      });
  }

}
