import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { errorMessageIfNeed, studyStatus } from 'src/app/utilities';
import { ForStudyListGQL, StudyFragment, StudyStatus, StartStadyGQL } from 'src/generated/graphql';



interface StudyRecord {
  studyId?: string;
  slideId: string;
  title: string;
  status: StudyStatus;
  studyStatus: string;
}

@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html'
})
export class StudyListComponent implements OnInit {
  dataLoad: Observable<StudyRecord[]> | null = null;

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
              return {
                slideId: study.slide.slideId,
                studyId: study.studyId,
                title: study.slide.config.title,
                status: study.status,
                studyStatus: studyStatus(study)
              }
            } else {
              return {
                slideId: study.slide.slideId,
                title: study.slide.config.title,
                status: StudyStatus.NotStart,
                studyStatus: studyStatus(<any>{ status: StudyStatus.NotStart })
              }
            }
          })
          return studies;
        }),
        share()
      );
  }

  onStartStudy(row: StudyRecord) {
    if (row.status == StudyStatus.NotStart) {
      this.startStudyGql.mutate({slideId: row.slideId})
      .subscribe(res => {
        if(!errorMessageIfNeed(res, this.snackBar)) {
          this.router.navigateByUrl(`/study/slide/${res.data?.startStudy}`)
        }
      })
    } else {
      this.router.navigateByUrl(`/study/slide/${row.studyId}`)
    }
  }

  statusPath(row: StudyRecord) {
    if (row.status == StudyStatus.NotStart) {
      return `/study/status/slide/${row.slideId}`
    }
    return `/study/status/${row.studyId}`
  }
}
