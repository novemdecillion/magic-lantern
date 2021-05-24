import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { NoticesGQL, DeleteNoticeGQL, NoticeFragment } from 'src/generated/graphql';
import { map, share } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { EditNoticeComponent } from '../edit-notice/edit-notice.component';
import { sortNotices } from 'src/app/utilities';

@Component({
  selector: 'app-notice-list',
  templateUrl: './notice-list.component.html',
  styles: [
  ]
})
export class NoticeListComponent implements OnInit {
  dataLoad: Observable<NoticeFragment[]> | null = null;

  constructor(private dialog: MatDialog, private noticesGql: NoticesGQL, private deleteNoticeGql: DeleteNoticeGQL) { }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.noticesGql.fetch()
      .pipe(
        map(res => sortNotices(res.data.notices)),
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

  onEditNotice(row: NoticeFragment) {
    this.dialog.open(EditNoticeComponent, { width: '400px', data: row })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onDeleteNotice(row: NoticeFragment) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '通知削除', message: `「${row.message}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.deleteNoticeGql.mutate({ noticeId: row.noticeId })
            .subscribe(_ => this.onLoadData());
        }
      });
  }
}
