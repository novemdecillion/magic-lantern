import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { errorMessageIfNeed, studyStatus } from 'src/app/utilities';
import { ForStudyListGQL, StudyFragment, StudyStatus, StartStadyGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html'
})
export class StudyListComponent implements OnInit {
  dataLoad: Observable<StudyFragment[]> | null = null;

  constructor(private router: Router,
    private snackBar: MatSnackBar,
    private myStudiesGql: ForStudyListGQL,
    private startStudyGql: StartStadyGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.myStudiesGql.fetch()
      .pipe(
        map(res => {
          let studies = res.data.studiesByUser
          .map(study => {
            if (study.__typename == 'Study') {
              return study as StudyFragment
            } else {
              return <any>{ slide: study.slide, status: StudyStatus.NotStart}
            }
          })
          return studies;
        }),
        share()
      );
  }

  onStartStudy(row: StudyFragment) {
    if (row.status == StudyStatus.NotStart) {
      this.startStudyGql.mutate({slideId: row.slide.slideId})
      .subscribe(res => {
        if(!errorMessageIfNeed(res, this.snackBar)) {
          this.router.navigateByUrl(`/study/slide/${res.data?.startStudy}`)
        }
      })
    } else {
      this.router.navigateByUrl(`/study/slide/${row.studyId}`)
    }
  }

  statusPath(row: StudyFragment) {
    if (row.status == StudyStatus.NotStart) {
      return `/study/status/slide/${row.slide.slideId}`
    }
    return `/study/status/${row.studyId}`
  }

  studyStatus(row: StudyFragment): string {
    return studyStatus(row)
  }
}
