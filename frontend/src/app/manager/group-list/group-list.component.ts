import { Component, OnInit, TemplateRef, ViewChild, ViewEncapsulation, OnDestroy } from '@angular/core';
import { ColumnDefinition } from 'src/app/share/list-page/list-page.component';
import { GroupFragment, GroupsGQL } from 'src/generated/graphql';
import { Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { EditGroupComponent } from '../edit-group/edit-group.component';
import { DEFAULT_GROUP_ID } from 'src/app/constants'
import { PageService } from 'src/app/share/page/page.service';

export interface GroupRecord extends GroupFragment {
  parentGroupName?: string
}

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GroupListComponent implements OnInit, OnDestroy {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  columns: ColumnDefinition<GroupRecord>[] = [];

  dataLoad: Observable<GroupRecord[]> | null = null;

  loadDataSubscription: Subscription;

  constructor(private groupsGql: GroupsGQL, public dialog: MatDialog, pageService: PageService) {
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.columns = [
      {
        name: 'groupName',
        displayName: 'グループ名'
      },
      {
        name: 'parentGroupName',
        displayName: '親グループ名'
      },
      {
        name: 'operation',
        displayName: null,
        sort: false,
        cellTemplate: this.operationTemplate
      }
    ];
    this.onLoadData();
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
  }

  onLoadData() {
    this.dataLoad = this.groupsGql.fetch()
      .pipe(map(res => {
        let groupToName: { [key: string]: string } = {};
        res.data.groups
          .forEach(group => {
            groupToName[group.groupId] = group.groupName;
          });

        let records = res.data.groups
          .map((group: GroupRecord) => {
            if (!!group.parentGroupId) {
              group.parentGroupName = groupToName[group.parentGroupId];
            }
            return group
          });
        return records;
      }));
  }

  onNewChildGroup(group: GroupRecord) {
    this.dialog.open(EditGroupComponent, { data: { group, type: 'new' } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onEditGroup(group: GroupRecord) {
    this.dialog.open(EditGroupComponent, { data: { group, type: 'edit' } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  canDeleteGroup(group: GroupRecord): boolean {
    return group.groupId !== DEFAULT_GROUP_ID
  }

  onDeleteGroup(group: GroupRecord) {
    this.dialog.open(EditGroupComponent, { data: { group, type: 'delete' } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }


}
