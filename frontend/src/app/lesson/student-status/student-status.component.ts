import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { convertToStudyStatus, StudyStatusRecord } from 'src/app/utilities';
import { SlideFragment, ForLessonStudentStatusGQL, UserFragment } from 'src/generated/graphql';
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-student-status',
  templateUrl: './student-status.component.html',
  styles: [
  ]
})
export class StudentStatusComponent implements OnInit {
  dataLoad: Observable<StudyStatusRecord[]> | null = null;
  slide!: SlideFragment;
  user!: UserFragment;

  constructor(
    activatedRoute: ActivatedRoute,
    studyGql: ForLessonStudentStatusGQL
  ) {
    let studyId = activatedRoute.snapshot.paramMap.get('studyId')!!;
    studyGql.fetch({ studyId })
    .subscribe(res => {
      if(res.data.study) {
        this.slide = res.data.study.slide;
        this.user = res.data.study.user;
        let records = convertToStudyStatus(this.slide, res.data.study);
        this.dataLoad = of(records);
      }
      // TODO エラー
    });

  }

  ngOnInit(): void {
  }

}
