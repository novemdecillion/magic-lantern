import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ListComponent } from 'src/app/share/list/list.component';
import { AddLessonGQL, PrepareAddLessonGQL, SlideFragment } from 'src/generated/graphql';
import { Observable, of } from 'rxjs';
import { createGroupNodes, GroupNode } from 'src/app/utilities';
import { MatRadioChange } from '@angular/material/radio';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface SelectableGroupNode extends GroupNode {
  selected?: boolean
}

@Component({
  selector: 'app-add-lesson',
  templateUrl: './add-lesson.component.html',
  styles: [
  ]
})
export class AddLessonComponent implements OnInit {
  @ViewChild('groupList', { static: true }) private groupList!: ListComponent<SelectableGroupNode>;

  slideDataLoad: Observable<SlideFragment[]> | null = null;
  groupDataLoad: Observable<GroupNode[]> | null = null;

  selectedSlide: string | null = null;
  selectedGroups: string[] | null = null;
  slideAssignedStatus: {[key: string]: string[]} = {};
  groupNodes: GroupNode[] = []

  constructor(
    private dialogRef: MatDialogRef<AddLessonComponent>,
    private snackBar: MatSnackBar,
    private prepareAddLessonGql: PrepareAddLessonGQL, private addLessonGql: AddLessonGQL) { }

  ngOnInit(): void {
    // this.slideColumns = [
    //   {
    //     name: 'selected',
    //     headerName: '選択',
    //     cellTemplate: this.selectSlideTemplate,
    //   },
    //   {
    //     name: 'slideId',
    //     headerName: '教材ID'
    //   },
    //   {
    //     name: 'title',
    //     valueFrom: (_, row) => row.config.title,
    //     headerName: '教材タイトル'
    //   }
    // ];

    // this.groupColumns = [
    //   {
    //     name: 'selected',
    //     headerName: '選択',
    //     cellTemplate: this.selectGroupTemplate,
    //     headerCellTemplate: this.selectGroupHeaderTemplate
    //   },
    //   {
    //     name: 'groupName',
    //     headerName: 'グループ名'
    //   },
    //   {
    //     name: 'parentGroupName',
    //     headerName: '所属グループ名'
    //   },
    // ];

    this.prepareAddLessonGql.fetch()
      .subscribe(res => {
        res.data.slides.forEach(slide => this.slideAssignedStatus[slide.slideId] = []);
        res.data.manageableLessons.forEach(lesson => this.slideAssignedStatus[lesson.slideId].push(lesson.groupId) );

        let slides = res.data.slides.filter(slide => this.slideAssignedStatus[slide.slideId].length != res.data.authoritativeGroupsByUser.length)

        if (0 == slides.length) {
          // 追加できる講座がない
          this.dialogRef.close(true);
          this.snackBar.open('追加できる講座はありません。', 'OK');
        } else {
          this.slideDataLoad = of(slides)

          this.groupNodes = createGroupNodes(res.data.authoritativeGroupsByUser)[1];
        }
      });
  }

  onOK() {
    if (!this.selectedSlide || !this.selectedGroups) {
      return
    }
    this.addLessonGql.mutate({command: { slideId: this.selectedSlide, groupIds: this.selectedGroups }})
        .subscribe(_ => this.dialogRef.close(true))
  }

  slideSelected(event: MatRadioChange, row: SlideFragment) {
    this.selectedSlide = row.slideId;
    this.groupDataLoad = of(this.groupNodes.filter(group => !this.slideAssignedStatus[row.slideId].includes(group.groupId)));
  }

  get selectAllGroup(): boolean | undefined {
    let selectedAll: boolean | undefined = undefined;
    this.groupList.dataSource.data.some((group, index)=> {
      let selected = group.selected ?? false
      if(0 == index) {
        selectedAll = selected
        return false
      } if (selectedAll != selected) {
        selectedAll = undefined;
        return true
      }
      return false
    })
    return selectedAll;
  }

  set selectAllGroup(selected: boolean | undefined) {
    this.groupList.dataSource.data.forEach(member => member.selected = selected);
    this.updateSelectedGroups();
  }

  onChangeGroup(event: MatCheckboxChange, row: SelectableGroupNode) {
    row.selected = event.checked;
    this.updateSelectedGroups();
  }

  updateSelectedGroups() {
    this.selectedGroups = this.groupList.dataSource.data.filter(group => group.selected).map(group => group.groupId)
    if (0 == this.selectedGroups?.length) {
      this.selectedGroups = null
    }
  }
}
