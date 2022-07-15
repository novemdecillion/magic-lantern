import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { NoticesGQL, DeleteNoticeGQL, NoticeFragment } from 'src/generated/graphql';
import { map, share } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { EditNoticeComponent } from '../edit-notice/edit-notice.component';
import { errorMessageIfNeed, NoticeRecord, sortNotices } from 'src/app/utilities';
import { MatSnackBar } from '@angular/material/snack-bar';
import { format, parseISO } from 'date-fns';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-notice-list',
  templateUrl: './notice-list.component.html',
  styles: [
  ]
})
export class NoticeListComponent implements OnInit {
  dataLoad: Observable<NoticeRecord[]> | null = null;

  constructor(
    private domSanitizer: DomSanitizer,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private noticesGql: NoticesGQL, private deleteNoticeGql: DeleteNoticeGQL) { }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.noticesGql.fetch()
      .pipe(
        map(res => {
          let notices = res.data.notices.map((notice: NoticeFragment) => {
            let safeHtmlNotice: NoticeRecord = Object.assign({
              safeHtmlMessage: this.domSanitizer.bypassSecurityTrustHtml(notice.message)
            }, notice);

            if (notice.startAt) {
              safeHtmlNotice.formattedStartAt = format(parseISO(notice.startAt), 'yyyy/MM/dd')
            }
            if (notice.endAt) {
              safeHtmlNotice.formattedEndAt = format(parseISO(notice.endAt), 'yyyy/MM/dd')
            }
            return safeHtmlNotice;
          })

          return sortNotices(notices);
        }),
        share()
      );
  }

  onAddNotice() {
    this.dialog.open(EditNoticeComponent, { width: '400px'})
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });

  }

  onEditNotice(row: NoticeRecord) {
    this.dialog.open(EditNoticeComponent, { width: '400px', data: row })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onDeleteNotice(row: NoticeRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '通知削除', message: `「${row.message}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(isOk => {
        if (isOk) {
          this.deleteNoticeGql.mutate({ noticeId: row.noticeId })
            .subscribe(res => {
              errorMessageIfNeed(res, this.snackBar)
              this.onLoadData()
            });
        }
      });
  }
}
