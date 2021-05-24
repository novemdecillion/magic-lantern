import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { MyStudiesGQL, StudyFragment, StudyStatus } from 'src/generated/graphql';

@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html'
})
export class StudyListComponent implements OnInit {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  dataLoad: Observable<StudyFragment[]> | null = null;

  constructor(private myStudiesGql: MyStudiesGQL) {
  }

  ngOnInit(): void {
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

  studyPath(row: StudyFragment) {
    if (!row.studyId) {
      return `/study/slide/start/${row.slide.slideId}`
    }
    return `/study/slide/${row.studyId}`
  }

  studyStatus(row: StudyFragment): string {
    if (!row.studyId) {
      return '未着手'
    }

    switch(row.status) {
      case StudyStatus.NotStart:
        return '未着手';
      case StudyStatus.OnGoing:
        return `実施中(${row.progressRate}%)`;
      case StudyStatus.Pass:
        let sumPass = this.studyScore(row)
        return `合格(${sumPass[0]}/${sumPass[1]}/${sumPass[2]})`;
      case StudyStatus.Failed:
        let sumFailed = this.studyScore(row)
        return `不合格(${sumFailed[0]}/${sumFailed[1]}/${sumFailed[2]})`;
    }
  }

  studyScore(study: StudyFragment): [number, number, number] {
    return study.scoreDetails.reduce((sum, chapter)=> {
      let sumQuestions = chapter.questions.reduce((prevSum, question)=>{
        return [prevSum[0] + question.scoring, prevSum[1] + question.score];
      }, [0, 0]);
      return [sum[0] + sumQuestions[0], sum[1] + chapter.passScore, sum[1] + sumQuestions[1]];
    }, [0, 0, 0]);
  }

}
