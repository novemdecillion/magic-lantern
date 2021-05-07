import { Component, OnInit } from '@angular/core';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { AddLessonGQL, GroupWithPathFragment, PrepareAddLessonGQL, SlideFragment } from 'src/generated/graphql';
import { Observable, of } from 'rxjs';
import { createGroupNodes, GroupNode } from 'src/app/utilities';

@Component({
  selector: 'app-add-lesson',
  templateUrl: './add-lesson.component.html',
  styles: [
  ]
})
export class AddLessonComponent implements OnInit {
  slideDataLoad: Observable<SlideFragment[]> | null = null;

  slideColumns: ColumnDefinition<SlideFragment>[] = [
    {
      name: 'slideId',
      headerName: '教材ID'
    },
    {
      name: 'title',
      valueFrom: (_, row) => row.config.title,
      headerName: '教材タイトル'
    }
  ];

  groupDataLoad: Observable<GroupNode[]> | null = null;

  groupColumns: ColumnDefinition<GroupNode>[] = [
    {
      name: 'groupName',
      headerName: 'グループ名'
    },
    {
      name: 'parentGroupName',
      headerName: '所属グループ名'
    },
];


  selectedSlide: string | null = null;
  selectedGroups: string[] | null = null;

  constructor(private prepareAddLessonGql: PrepareAddLessonGQL, private addLessonGql: AddLessonGQL) { }

  ngOnInit(): void {

    this.prepareAddLessonGql.fetch()
    .subscribe(res => {
      let slideAssignedStatus: {[key: string]: string[]} = {};
      res.data.slides.forEach(slide => slideAssignedStatus[slide.slideId] = []);
      res.data.manageableLessons.forEach(lesson => slideAssignedStatus[lesson.lesson.slideId].push(lesson.lesson.groupId) );

      this.slideDataLoad = of(res.data.slides)


      let [_, groupNodes] = createGroupNodes(res.data.authoritativeGroups);
      this.groupDataLoad = of(groupNodes);
    })
  }

  onOK() {
  }

}
