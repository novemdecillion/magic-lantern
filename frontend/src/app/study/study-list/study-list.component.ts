import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { MyStudiesGQL, StudyFragment } from 'src/generated/graphql';

@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html'
})
export class StudyListComponent implements OnInit {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  dataLoad: Observable<StudyFragment[]> | null = null;

  // columns: ColumnDefinition<StudyFragment>[] = [];

  constructor(private myStudiesGql: MyStudiesGQL) {
  }

  ngOnInit(): void {
    // this.columns = [
    //   {
    //     name: 'slideTitle',
    //     headerName: '教材タイトル',
    //     valueFrom: (_, row): string => row.config.title
    //   },
    //   {
    //     name: 'operation',
    //     headerName: null,
    //     sort: false,
    //     cellTemplate: this.operationTemplate
    //   }
    // ];

    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.myStudiesGql.fetch()
      .pipe(
        map(res => {
          return res.data.myStudies
        }),
        share()
      );
  }

  // title = (_: ColumnDefinition<StudyFragment>, row: StudyFragment): string => row.slide.config.title
  studyPath(row: StudyFragment) {
    if (!row.studyId) {
      return `/study/slide/start/${row.slide.slideId}`
    }
    return `/study/slide/${row.studyId}`
  }

  studyStatus(row: StudyFragment) {
    if (!row.studyId) {
      return '未着手'
    }
    // TODO
    return '実施中'
  }

}
