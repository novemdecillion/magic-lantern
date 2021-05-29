import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ForLessonStudentListGQL, GroupFragment, SlideFragment } from 'src/generated/graphql';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { studyStatus } from 'src/app/utilities';

interface StudentRecord {
  userName: string;
  status: string;
  studyId?: string;
}

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styles: [
  ]
})
export class StudentListComponent implements OnInit {
  dataLoad: Observable<StudentRecord[]> | null = null;

  lessonId: string
  slide: SlideFragment | null = null;
  group: GroupFragment | null = null;

  constructor(
    private router: Router,
    activatedRoute: ActivatedRoute,
    private lessonStudentListGql: ForLessonStudentListGQL
  ) {
    this.lessonId = activatedRoute.snapshot.paramMap.get('lessonId')!!;
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.lessonStudentListGql.fetch({lessonId: this.lessonId})
      .pipe(
        map(res=> {
          if (res.data.lesson) {
            this.group = res.data.lesson.group;
            this.slide = res.data.lesson.slide;
          }

          let records: StudentRecord[] = [];

          res.data.lessonStudies
            .forEach(study => {
              records.push({
                userName: study.user.userName,
                status: studyStatus(study),
                studyId: study.studyId
              })
            });

          res.data.notStartLessonStudies
            .forEach(user => {
              records.push({
                userName: user.userName,
                status: '未着手'
              })
            });

          return records;
        })
      );
  }


}
