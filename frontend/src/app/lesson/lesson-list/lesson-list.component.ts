import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { DeleteLessonGQL, LessonWithStatisticsFragment, ManageableLessonsGQL } from 'src/generated/graphql';
import { MatDialog } from '@angular/material/dialog';
import { AddLessonComponent } from '../add-lesson/add-lesson.component';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { errorMessageIfNeed } from 'src/app/utilities';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-lesson-list',
  templateUrl: './lesson-list.component.html'
})
export class LessonListComponent implements OnInit {
  dataLoad: Observable<LessonWithStatisticsFragment[]> | null = null;

  constructor(public dialog: MatDialog,
    private snackBar: MatSnackBar,
    private lessonsGql: ManageableLessonsGQL, private deleteLessonGql: DeleteLessonGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.lessonsGql.fetch()
      .pipe(
        map(res => {
          return res.data.manageableLessons
        }),
        share()
      );
  }

  onAddLesson() {
    this.dialog.open(AddLessonComponent)
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onDeleteLesson(row: LessonWithStatisticsFragment) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '講座削除', message: `グループ「${row.group.groupName}」より教材「${row.slide.config.title}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.deleteLessonGql.mutate({ lessonId: row.lessonId })
            .subscribe(res => {
              if(!errorMessageIfNeed(res, this.snackBar)) {
                this.onLoadData();
              }
            })

          }
      });
  }


}
