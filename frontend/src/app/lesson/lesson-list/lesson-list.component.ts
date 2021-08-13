import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { DeleteLessonGQL, LessonWithStatisticsFragment, ManageableLessonsGQL } from 'src/generated/graphql';
import { MatDialog } from '@angular/material/dialog';
import { AddLessonComponent } from '../add-lesson/add-lesson.component';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { errorMessageIfNeed } from 'src/app/utilities';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface LessonRecord {
  lessonId: string;
  title: string;
  studentCount: number;
  notStartCount: number;
  onGoingCount: number;
  passCount: number;
  failCount: number;
  excludedCount: number;
}

@Component({
  selector: 'app-lesson-list',
  templateUrl: './lesson-list.component.html'
})
export class LessonListComponent implements OnInit {
  dataLoad: Observable<LessonRecord[]> | null = null;

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
          return res.data.manageableLessons.map(row => {
            return {
              lessonId: row.lessonId,
              title: `${row.group.groupName} - ${row.slide.title}`,
              studentCount: row.studentCount,
              notStartCount: row.studentCount - row.statistics.onGoingCount - row.statistics.passCount - row.statistics.failCount - row.statistics.excludedCount,
              onGoingCount: row.statistics.onGoingCount,
              passCount: row.statistics.passCount,
              failCount: row.statistics.failCount,
              excludedCount: row.statistics.excludedCount
            }
          })
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

  onDeleteLesson(row: LessonRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '講座削除', message: `講座「${row.title}」を削除します。よろしですか?` } })
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
