import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ForLessonStudentListGQL, GroupFragment, SlideFragment, StudyStatus, ChangeStudyStatusGQL, AnswerDetailsFragment } from 'src/generated/graphql';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createGroupPathNameByGroups, downloadBlob, errorMessageIfNeed, studyStatus, studyStatusDefine } from 'src/app/utilities';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ListComponent } from 'src/app/share/list/list.component';
import { format, parseISO } from 'date-fns';

interface StudentRecord {
  userId: string;
  userName: string;
  email?: string;
  no: number;
  status: string;
  studyId?: string;
  studyStatus: StudyStatus;
  startAt?: any;
  endAt?: any;
  progressRate?: number,
  answerDetails?: AnswerDetailsFragment[]
}

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styles: [
  ]
})
export class StudentListComponent implements OnInit {
  @ViewChild(ListComponent, { static: true }) private list!: ListComponent<StudentRecord>;

  dataLoad: Observable<StudentRecord[]> | null = null;

  lessonId: string
  slide: SlideFragment | null = null;
  group: GroupFragment | null = null;

  constructor(
    activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private lessonStudentListGql: ForLessonStudentListGQL,
    private changeStudyStatusGQL: ChangeStudyStatusGQL,
    private hostElementRef: ElementRef<HTMLElement>
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
                  no: (study.index) ? study.index + 1 : 1,
                  status: studyStatus(study),
                  studyId: study.studyId,
                  studyStatus: study.status,
                  startAt: study.startAt,
                  endAt: study.endAt,
                  progressRate: study.progressRate ?? undefined,
                  answerDetails: study.answerDetails
                })
              } else {
                records.push({
                  userId: study.userId,
                  userName: study.user.userName,
                  email: study.user.email ?? undefined,
                  no: 1,
                  status: studyStatusDefine[StudyStatus.NotStart].name,
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
    switch (row.studyStatus) {
      case StudyStatus.OnGoing:
      case StudyStatus.Excluded:
        return (row.no == 1);

      default:
        return false;
    }
  }

  onChangeNotStart(row: StudentRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '未着手に変更', message: `この操作により、${row.userName}の学習状況は削除されます。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.changeStudyStatusGQL
          .mutate({ command: { studyId: row.studyId, slideId: this.slide!!.slideId, userId: row.userId, index: row.no - 1, status: StudyStatus.NotStart } })
          .subscribe(res => {
            if(!errorMessageIfNeed(res, this.snackBar)) {
              this.onLoadData();
            }
          })
        }
      });
  }

  canChangeExcluded(row: StudentRecord): boolean {
    switch (row.studyStatus) {
      case StudyStatus.OnGoing:
      case StudyStatus.NotStart:
        return (row.no == 1);

      default:
        return false;
    }
  }

  onChangeExcluded(row: StudentRecord) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '対象外に変更', message: `${row.userName}を対象外にします。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.changeStudyStatusGQL
          .mutate({ command: { studyId: row.studyId, slideId: this.slide!!.slideId, userId: row.userId, index: row.no - 1, status: StudyStatus.Excluded } })
          .subscribe(res => {
            if(!errorMessageIfNeed(res, this.snackBar)) {
              this.onLoadData();
            }
          })
      }
      });
  }

  onExportStudentStatus() {
    const data: any[][] = [];

    data.push(['グループ', '所属グループ', '教材タイトル']);
    data.push([
      this.group?.groupName,
      (this.group?.path != null)? createGroupPathNameByGroups(this.group.path): null,
      this.slide?.title]);
    data.push([]);

    interface QuestionSummary {
      chapterIndex: number;
      questionCount: number;
    }
    let chapterAndQuestionIndexes: QuestionSummary[] = [];
    let header = ['氏名', 'メールアドレス', '状態', '受講順番', '開始日時', '終了日時', '進捗率']
    this.slide?.chapters?.map((chapter, chapterIndex) => {
      if (chapter.__typename == 'ExamChapter') {
        let qSummary: QuestionSummary = { chapterIndex, questionCount: 0 };
        chapterAndQuestionIndexes.push(qSummary);

        for(let questIndex = 0; questIndex < chapter.numberOfAllQuestions; questIndex++) {
          qSummary.questionCount++;
          header.push(`${chapter.title}-問${questIndex + 1}`)
        }
      } else if (chapter.__typename == 'SurveyChapter') {
        let qSummary: QuestionSummary = { chapterIndex, questionCount: 0 };
        chapterAndQuestionIndexes.push(qSummary);

        for(let questIndex = 0; questIndex < chapter.numberOfQuestions; questIndex++) {
          qSummary.questionCount++;
          header.push(`${chapter.title}-問${questIndex + 1}`)
        }
      }
    })
    let chapterToQuestionStartIndex : {[key: number]: number} = {}
    let questionCount = chapterAndQuestionIndexes.reduce((prev, current) => {
      chapterToQuestionStartIndex[current.chapterIndex] = prev;
      return prev + current.questionCount
    }, 0);

    data.push(header);
    data.push();

    this.list.dataSource.data.forEach(study => {

      let line = [
        study.userName, study.email,
        studyStatusDefine[study.studyStatus].name,
        study.no
      ];
      switch (study.studyStatus) {
        case StudyStatus.NotStart:
        case StudyStatus.Excluded:
          break;
        default:
          line = line.concat([
            (study.startAt != null) ? format(parseISO(study.startAt), 'yyyy/MM/dd HH:mm'): undefined,
            (study.endAt != null) ? format(parseISO(study.endAt), 'yyyy/MM/dd HH:mm'): undefined,
            study.progressRate?.toString()
          ])

          let answers = new Array(questionCount)
          study.answerDetails?.forEach(anser => {
            let startIndex = chapterToQuestionStartIndex[anser.chapterIndex]

            anser.questions.forEach(question => {
              answers[startIndex + question.questionIndex] = question.answers.toString();
            });
          })
          line = line.concat(answers);
          break;
      }
      data.push(line);
    });
    const bom  = new Uint8Array([0xEF, 0xBB, 0xBF]);
    const blob = new Blob([bom, this.escapeAllForCSV(data)], {type: 'text/csv'});
    const url = window.URL.createObjectURL(blob);

    // let link = document.createElement('a');
    // link.setAttribute('style', 'display: none');
    // link.download = ;
    // link.href = url;
    // document.body.appendChild(link);
    // link.click();
    // document.body.removeChild(link);
    // window.URL.revokeObjectURL(url);

    downloadBlob(`講座状況-${this.group?.groupName}-${this.slide?.title}-${format(new Date(), 'yyyyMMddHHmm')}.csv`,
      blob, this.hostElementRef.nativeElement);
  }

  escapeForCSV(s: string | undefined | null): string {
    if ((s === null) || (s === undefined)) {
      return '';
    }
    return `"${s.replace(/\"/g, '\"\"')}"`
  }

  escapeAllForCSV(arr: any[][], colDelimeter=',', rowDelimeter='\n') {
    return arr.map((row) => row.map((cell) => this.escapeForCSV(cell)).join(colDelimeter)).join(rowDelimeter);
  }

}
