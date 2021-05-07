import { Component, OnInit } from '@angular/core';
import { StudiableLessonsGQL, LessonWithRelationFragment } from 'src/generated/graphql';

@Component({
  selector: 'app-lesson-list',
  templateUrl: './lesson-list.component.html'
})
export class LessonListComponent implements OnInit {
  displayedColumns: string[] = ['title'];
  lessons: LessonWithRelationFragment[] = [];

  constructor(private lessonsGql: StudiableLessonsGQL) { }

  ngOnInit(): void {
    this.lessonsGql.fetch().subscribe(res => {
      this.lessons = res.data.studiableLessons ?? [];
    })
  }

  extractTitle(data: LessonWithRelationFragment, name: string): string | undefined {
    // return data?.slide?.title;
    return undefined
  }
}
