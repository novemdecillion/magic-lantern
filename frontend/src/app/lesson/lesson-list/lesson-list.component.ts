import { Component, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { ColumnDefinition } from 'src/app/share/list-page/list-page.component';
import { map, share } from 'rxjs/operators';
import { PageService } from 'src/app/share/page/page.service';
import { LessonWithRelationFragment, ManageableLessonsGQL } from 'src/generated/graphql';
import { MatDialog } from '@angular/material/dialog';
import { AddLessonComponent } from '../add-lesson/add-lesson.component';

@Component({
  selector: 'app-lesson-list',
  templateUrl: './lesson-list.component.html'
})
export class LessonListComponent implements OnInit {

  loadDataSubscription: Subscription;
  dataLoad: Observable<LessonWithRelationFragment[]> | null = null;

  columns: ColumnDefinition<LessonWithRelationFragment>[] = [
    {
      name: 'groupName',
      headerName: 'グループ',
      valueFrom: (_, row): string => row.group.group.groupName
    },
    {
      name: 'slideTitle',
      headerName: '教材タイトル',
      valueFrom: (_, row): string => row.slide.config.title
    },
  ];

  constructor(private lessonsGql: ManageableLessonsGQL, public dialog: MatDialog, pageService: PageService) {
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
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
}
