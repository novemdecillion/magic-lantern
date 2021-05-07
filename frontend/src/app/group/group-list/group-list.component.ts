import { Component, OnInit, TemplateRef, ViewChild, OnDestroy } from '@angular/core';
import { ColumnDefinition } from 'src/app/share/list-page/list-page.component';
import { EffectiveGroupsGQL } from 'src/generated/graphql';
import { Observable, Subscription } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { EditGroupComponent } from '../edit-group/edit-group.component';
import { DEFAULT_GROUP_ID } from 'src/app/constants'
import { PageService } from 'src/app/share/page/page.service';
import { createGroupNodes, GroupNode } from '../../utilities';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit, OnDestroy {
  @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  columns: ColumnDefinition<GroupNode>[] = [];

  dataLoad: Observable<GroupNode[]> | null = null;

  loadDataSubscription: Subscription;

  constructor(private groupsGql: EffectiveGroupsGQL, public dialog: MatDialog, pageService: PageService) {
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.columns = [
      {
        name: 'groupName',
        headerName: 'グループ名'
      },
      {
        name: 'parentGroupName',
        headerName: '所属グループ名'
      },
      {
        name: 'operation',
        headerName: null,
        sort: false,
        cellTemplate: this.operationTemplate
      }
    ];
    this.onLoadData();
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
  }

  onLoadData(): void {
    this.dataLoad = this.groupsGql.fetch()
      .pipe(
        map(res => {
          let [nodes, _] = createGroupNodes(res.data.effectiveGroups)
          return Object.values(nodes);
        }),
        share()
      );
  }

  onNewChildGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'new' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  onEditGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'edit' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }

  canDeleteGroup(group: GroupNode): boolean {
    return group.groupId !== DEFAULT_GROUP_ID
  }

  onDeleteGroup(group: GroupNode): void {
    this.dialog.open(EditGroupComponent, { data: Object.assign({ type: 'delete' }, group) })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });
  }
}
