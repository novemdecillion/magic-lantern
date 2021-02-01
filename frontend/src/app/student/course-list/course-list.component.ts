import { Component, OnInit } from '@angular/core';
import { CoursesGQL, CourseFragment } from 'src/generated/graphql';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.scss']
})
export class CourseListComponent implements OnInit {
  displayedColumns: string[] = ['title'];
  courses: CourseFragment[] = [];

  constructor(private coursesGql: CoursesGQL) { }

  ngOnInit(): void {
    this.coursesGql.fetch().subscribe(res => {
      this.courses = res.data.courses ?? [];
    })
  }

  extractTitle(data: CourseFragment, name: string): string | undefined {
    return data.slide?.title;
  }
}
