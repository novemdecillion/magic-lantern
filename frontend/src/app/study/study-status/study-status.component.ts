import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationBehaviorOptions, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { convertToStudyStatus, StudyStatusRecord } from 'src/app/utilities';
import { StartStadyGQL, SlideGQL, StudyFragment, StudyGQL, SlideFragment } from 'src/generated/graphql';


@Component({
  selector: 'app-study-status',
  templateUrl: './study-status.component.html'
})
export class StudyStatusComponent implements OnInit {
  dataLoad: Observable<StudyStatusRecord[]> | null = null;

  studyId?: string;
  slide!: SlideFragment;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private startStudyGql: StartStadyGQL,
    slideGql: SlideGQL,
    studyGql: StudyGQL
  ) {
    this.studyId = this.activatedRoute.snapshot.paramMap.get('studyId')!!;
    if (this.studyId) {
      studyGql.fetch({ studyId: this.studyId })
        .subscribe(res => {
          if(res.data.study) {
            this.slide = res.data.study.slide;
            let records = convertToStudyStatus(this.slide, res.data.study);
            this.dataLoad = of(records);
          }
          // TODO エラー
        });
    } else {
      let slideId = this.activatedRoute.snapshot.paramMap.get('slideId')!!;
      slideGql.fetch({ slideId })
        .subscribe(res => {
          if(res.data.slide) {
            this.slide = res.data.slide;
            let records = convertToStudyStatus(this.slide);
            this.dataLoad = of(records);
          }
          // TODO エラー
        });
    }
  }

  ngOnInit(): void {
  }

  // convert(study?: StudyFragment | null) {
  //   let records = this.slide?.config
  //     ?.chapters?.map((chapter, index) => {
  //       let record: StudyStatusRecord = {
  //         chapterIndex: index,
  //         title: chapter.title,
  //         status: '未着手',
  //         pages: 0,
  //         numberOfPages: chapter.numberOfPages
  //       };
  //       if (chapter.__typename == 'ExamChapter') {
  //         record.passScore = chapter.passScore ?? 0;
  //         record.totalScore = chapter.questions.reduce((acc, value) => acc + (value.score ?? 0), 0);
  //       }
  //       return record
  //     })
  //     ?? [];

  //   if (study) {
  //     study.progressDetails.forEach(prog => {
  //       records[prog.chapterIndex].pages = prog.pageIndexes.length;
  //     })
  //     study.scoreDetails.forEach(score => {
  //       records[score.chapterIndex].score = score.questions.reduce((acc, value) => acc + (value.scoring ?? 0), 0);
  //     })
  //   }

  //   records.forEach(record => {
  //     if ((undefined != record.score)
  //         && (undefined != record.passScore)
  //         && (undefined != record.totalScore)) {
  //       if (record.passScore <= record.score) {
  //         record.status = `合格 (得点:${record.score}点 / 合格:${record.passScore}点)`
  //       } else {
  //         record.status = `不合格 (得点:${record.score}点 / 合格:${record.passScore}点)`
  //       }
  //     } else if (record.pages == record.numberOfPages) {
  //       record.status = '済';
  //     } else if (0 < record.pages) {
  //       record.status = '実施中';
  //     }
  //   })
  //   this.dataLoad = of(records);
  // }

  onStartStudy(row: StudyStatusRecord) {
    let state: NavigationBehaviorOptions = { state: { chapter: row.chapterIndex }}
    if (!this.studyId) {
      this.startStudyGql.mutate({slideId: this.slide.slideId})
      .subscribe(res => this.router.navigateByUrl(`/study/slide/${res.data?.startStudy}`, state))
    } else {
      this.router.navigateByUrl(`/study/slide/${this.studyId}`, state)
    }
  }
}
