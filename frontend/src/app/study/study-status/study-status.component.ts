import { Component, OnInit } from '@angular/core';
import { MatSelectionListChange } from '@angular/material/list';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationBehaviorOptions, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { convertToChapterStatus, errorMessageIfNeed, ChapterStatusRecord, studyStatusDefine } from 'src/app/utilities';
import { StartStadyGQL, SlideFragment, StudiesByUserGQL } from 'src/generated/graphql';

interface StudyStatusRecord {
  index: number;
  status: string;
  studyId?: string;
  records: ChapterStatusRecord[];
}

@Component({
  selector: 'app-study-status',
  templateUrl: './study-status.component.html'
})
export class StudyStatusComponent implements OnInit {
  dataLoad: Observable<ChapterStatusRecord[]> | null = null;

  studyId?: string;
  slide!: SlideFragment;

  studyRecords!: StudyStatusRecord[];
  studyIndex!: number

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private snackBar: MatSnackBar,
    private startStudyGql: StartStadyGQL,
    studyGql: StudiesByUserGQL
  ) {
    let slideId = this.activatedRoute.snapshot.paramMap.get('slideId')!!;

    studyGql.fetch({ slideId })
        .subscribe(res => {
          errorMessageIfNeed(res, this.snackBar)

          this.studyRecords = res.data.studiesByUser
            .map(study => {
              if (!this.slide) {
                this.slide = study.slide;
              }

              if (study.__typename == 'Study') {
                return <StudyStatusRecord>{
                  index: (study.index ?? 0),
                  studyId: study.studyId,
                  status: studyStatusDefine[study.status].name,
                  records: convertToChapterStatus(study.slide, study)
                };
              } else {
                return <StudyStatusRecord>{
                  index: 0,
                  status: studyStatusDefine[study.status].name,
                  records: convertToChapterStatus(study.slide)
                }
              }
            })
            .sort((a, b) => a.index - b.index);

          this.studyIndex = this.studyRecords.length - 1;

          let studyStatus = this.studyRecords[this.studyIndex]
          this.dataLoad = of(studyStatus.records);
          this.studyId = studyStatus.studyId
        });
  }

  ngOnInit(): void {
  }

  onStartStudy(row: ChapterStatusRecord) {
    let state: NavigationBehaviorOptions = { state: { chapter: row.chapterIndex }}
    if (!this.studyId) {
      this.startStudyGql.mutate({slideId: this.slide.slideId})
      .subscribe(res => {
        if(!errorMessageIfNeed(res, this.snackBar)) {
          this.router.navigateByUrl(`/study/slide/${res.data?.startStudy}`, state)
        }
      })
    } else {
      this.router.navigateByUrl(`/study/slide/${this.studyId}`, state)
    }
  }

  onChangeStudy(event: MatSelectionListChange) {
    this.studyIndex = event.options[0].value;

    let studyStatus = this.studyRecords[this.studyIndex]
    this.dataLoad = of(studyStatus.records);
    this.studyId = studyStatus.studyId
  }

}
