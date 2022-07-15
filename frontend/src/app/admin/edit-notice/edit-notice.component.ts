import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import formatISO from 'date-fns/formatISO';
import parseISO from 'date-fns/parseISO';
import { errorMessageIfNeed, NoticeRecord } from 'src/app/utilities';
import { AddNoticeGQL, UpdateNoticeGQL } from 'src/generated/graphql';

// type NoticeRecord = NoticeFragment & {
//   parsedStartAt: Date | null
//   parsedEndAt: Date | null
// }

@Component({
  selector: 'app-edit-notice',
  templateUrl: './edit-notice.component.html',
  styleUrls: ['./edit-notice.component.scss']
})
export class EditNoticeComponent {
  isEdit: boolean
  notice: NoticeRecord

  constructor(
    private dialogRef: MatDialogRef<EditNoticeComponent>,
    @Inject(MAT_DIALOG_DATA) notice: NoticeRecord,
    private snackBar: MatSnackBar,
    private addNoticeGql: AddNoticeGQL, private updateNoticeGql: UpdateNoticeGQL
  ) {
    if (notice) {
      this.isEdit = true;
      this.notice = Object.assign({}, notice);
      delete this.notice.__typename;
      delete this.notice.updateAt;

      if (this.notice.startAt) {
        this.notice.startAt = parseISO(this.notice.startAt)
      }
      if (this.notice.endAt) {
        this.notice.endAt = parseISO(this.notice.endAt)
      }

    } else {
      this.isEdit = false;
      this.notice = <any>{}
    }
  }

  onOK(): void {
    let startAt = null;
    if (this.notice.startAt) {
      startAt = formatISO(this.notice.startAt, { representation: 'date' })
    }
    let endAt = null;
    if (this.notice.endAt) {
      endAt = formatISO(this.notice.endAt, { representation: 'date' })
    }

    if (startAt && endAt && endAt < startAt) {
      this.snackBar.open('掲載開始日は終了日より前にしてください。', '確認');
      return
    }

    if (this.isEdit) {
      this.updateNoticeGql.mutate({command: {
        noticeId: this.notice.noticeId,
        message: this.notice.message,
        startAt,
        endAt
      }})
        .subscribe(res => {
          if(!errorMessageIfNeed(res, this.snackBar)) {
            this.dialogRef.close(true)
          }
        });
    } else {
      this.addNoticeGql.mutate({command: {
        message: this.notice.message,
        startAt,
        endAt
      }})
        .subscribe(res => {
          if(!errorMessageIfNeed(res, this.snackBar)) {
            this.dialogRef.close(true)
          }
        });
    }
  }
}
