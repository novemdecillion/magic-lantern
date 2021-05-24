import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import formatISO from 'date-fns/formatISO';
import parseISO from 'date-fns/parseISO';
import { NoticeFragment, AddNoticeGQL, UpdateNoticeGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-edit-notice',
  templateUrl: './edit-notice.component.html',
  styleUrls: ['./edit-notice.component.scss']
})
export class EditNoticeComponent {
  isEdit: boolean
  notice: NoticeFragment

  constructor(
    private dialogRef: MatDialogRef<EditNoticeComponent>,
    @Inject(MAT_DIALOG_DATA) notice: NoticeFragment,
    private addNoticeGql: AddNoticeGQL, private updateNoticeGql: UpdateNoticeGQL
  ) {
    if (notice) {
      this.isEdit = true;
      this.notice = Object.assign({}, notice);
      delete this.notice.__typename;

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
    if (this.notice.startAt) {
      this.notice.startAt = formatISO(this.notice.startAt, { representation: 'date' })
    }
    if (this.notice.endAt) {
      this.notice.endAt = formatISO(this.notice.endAt, { representation: 'date' })
    }

    if (this.isEdit) {
      this.updateNoticeGql.mutate({command: this.notice})
        .subscribe(_ => this.dialogRef.close(true))
    } else {
      this.addNoticeGql.mutate({command: this.notice})
        .subscribe(_ => this.dialogRef.close(true))
    }
  }
}
