import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ForLessonStudentListGQL, GroupFragment, SlideFragment, StudyStatus, ChangeStudyStatusGQL } from 'src/generated/graphql';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { errorMessageIfNeed, studyStatus } from 'src/app/utilities';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

interface StudentRecord {
  userId: string;
  userName: string;
  email?: string;
  status: string;
  studyId?: string;
  studyStatus: StudyStatus;
}

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styles: [
  ]
})
export class StudentListComponent implements OnInit {
  dataLoad: Observable<StudentRecord[]> | null = null;

  lessonId: string
  slide: SlideFragment | null = null;
  group: GroupFragment | null = null;

  constructor(
    activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private lessonStudentListGql: ForLessonStudentListGQL,
    private changeStudyStatusGQL: ChangeStudyStatusGQL
  ) {
    this.lessonId = activatedRoute.snapshot.paramMap.get('lessonId')!!;
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.lessonStudentListGql.fetch({lessonId: this.lessonId})
      .pipe(
        map(res=> {
          if (res.data.lesson) {
            this.group = res.data.lesson.group;
            this.slide = res.data.lesson.slide;
          }

          let records: StudentRecord[] = []
          res.data.lessonStudies
            .forEach(study => {
              if (study.__typename == 'Study') {
                records.push({
                  userId: study.userId,
                  userName: study.user.userName,
                  email: study.user.email ?? undefined,
                  status: studyStatus(study),
                  studyId: study.studyId,
                  studyStatus: study.status
                })
              } else {
                records.push({
                  userId: study.userId,
                  userName: study.user.userName,
                  email: study.user.email ?? undefined,
                  status: '未着手',
                  studyStatus: StudyStatus.NotStart
                })
              }
            });
          return records;
        })
      );
  }

  isShowStudentStatusLink(row: StudentRecord): boolean {
    switch (row.studyStatus) {
      case StudyStatus.NotStart:
      case StudyStatus.Excluded:
        return false;
      default:
        return true;
    }
  }


  canChangeNotStart(row: StudentRecord): boolean {
    return row.studyStatus != StudyStatus.NotStart;
  }

  onChangeNotStart(row: StudentRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '未着手に変更', message: `この操作により、${row.userName}の学習状況は削除されます。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.changeStudyStatusGQL
          .mutate({ command: { studyId: row.studyId, slideId: this.slide!!.slideId, userId: row.userId, status: StudyStatus.NotStart } })
          .subscribe(res => {
            if(!errorMessageIfNeed(res, this.snackBar)) {
              this.onLoadData();
            }
          })
        }
      });
  }

  canChangeExcluded(row: StudentRecord): boolean {
    return row.studyStatus != StudyStatus.Excluded;
  }

  onChangeExcluded(row: StudentRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '対象外に変更', message: `${row.userName}を対象外にします。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.changeStudyStatusGQL
          .mutate({ command: { studyId: row.studyId, slideId: this.slide!!.slideId, userId: row.userId, status: StudyStatus.Excluded } })
          .subscribe(res => {
            if(!errorMessageIfNeed(res, this.snackBar)) {
              this.onLoadData();
            }
          })
      }
      });
  }

}
