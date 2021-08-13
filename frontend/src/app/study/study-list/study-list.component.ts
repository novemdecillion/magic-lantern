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
  index: number;
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
          let studies = res.data.latestStudiesByUser
          .map(study => {
            if (study.__typename == 'Study') {
              return <StudyRecord>{
                slideId: study.slide.slideId,
                studyId: study.studyId,
                title: study.slide.title,
                index: (study.index ?? 0) + 1,
                status: study.status,
                studyStatus: studyStatus(study)
              }
            } else {
              return <StudyRecord>{
                slideId: study.slide.slideId,
                title: study.slide.title,
                index: 1,
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

  onStartStudy(row: StudyRecord, restart: boolean = false) {
    if ((row.status == StudyStatus.NotStart) || (restart)) {
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
    // if (row.status == StudyStatus.NotStart) {
    //   return `/study/status/slide/${row.slideId}`
    // }
    // return `/study/status/${row.studyId}`
    return `/study/status/${row.slideId}`
  }

  canStartStudy(row: StudyRecord): boolean {
    switch (row.status) {
      case StudyStatus.NotStart:
      case StudyStatus.OnGoing:
        return true;
      default:
        return false;
    }
  }

  canOperation(row: StudyRecord): boolean {
    switch (row.status) {
      case StudyStatus.Pass:
      case StudyStatus.Failed:
        return true;
      default:
        return false;
    }
  }
}
