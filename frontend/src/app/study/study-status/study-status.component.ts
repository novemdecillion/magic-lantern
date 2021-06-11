import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationBehaviorOptions, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { convertToStudyStatus, errorMessageIfNeed, StudyStatusRecord } from 'src/app/utilities';
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
    private snackBar: MatSnackBar,
    private startStudyGql: StartStadyGQL,
    slideGql: SlideGQL,
    studyGql: StudyGQL
  ) {
    this.studyId = this.activatedRoute.snapshot.paramMap.get('studyId')!!;
    if (this.studyId) {
      studyGql.fetch({ studyId: this.studyId })
        .subscribe(res => {
          errorMessageIfNeed(res, this.snackBar)
          if(res.data.study) {
            this.slide = res.data.study.slide;
            let records = convertToStudyStatus(this.slide, res.data.study);
            this.dataLoad = of(records);
          }
        });
    } else {
      let slideId = this.activatedRoute.snapshot.paramMap.get('slideId')!!;
      slideGql.fetch({ slideId })
        .subscribe(res => {
          errorMessageIfNeed(res, this.snackBar)
          if(res.data.slide) {
            this.slide = res.data.slide;
            let records = convertToStudyStatus(this.slide);
            this.dataLoad = of(records);
          }
        });
    }
  }

  ngOnInit(): void {
  }

  onStartStudy(row: StudyStatusRecord) {
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
}
