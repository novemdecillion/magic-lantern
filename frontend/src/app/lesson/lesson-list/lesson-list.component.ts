import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { map, share } from 'rxjs/operators';
import { DeleteLessonGQL, LessonFragment, ManageableLessonsGQL } from 'src/generated/graphql';
import { MatDialog } from '@angular/material/dialog';
import { AddLessonComponent } from '../add-lesson/add-lesson.component';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-lesson-list',
  templateUrl: './lesson-list.component.html'
})
export class LessonListComponent implements OnInit {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  dataLoad: Observable<LessonFragment[]> | null = null;

  columns: ColumnDefinition<LessonFragment>[] = [];

  constructor(public dialog: MatDialog,
      private lessonsGql: ManageableLessonsGQL, private deleteLessonGql: DeleteLessonGQL) {
  }

  ngOnInit(): void {
    this.columns = [
      {
        name: 'groupName',
        headerName: 'グループ',
        valueFrom: (_, row): string => row.group.groupName
      },
      {
        name: 'slideTitle',
        headerName: '教材タイトル',
        valueFrom: (_, row): string => row.slide.config.title
      },
      {
        name: 'operation',
        headerName: null,
        sort: false,
        cellTemplate: this.operationTemplate
      }
    ];

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

  onDeleteLesson(row: LessonFragment) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '講座削除', message: `グループ「${row.group.groupName}」より教材「${row.slide.config.title}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.deleteLessonGql.mutate({ lessonId: row.lessonId })
            .subscribe(_ => this.onLoadData());
        }
      });
  }


}
