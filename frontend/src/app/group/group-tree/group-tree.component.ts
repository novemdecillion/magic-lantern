import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { Observable, Subscription } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { EffectiveGroupsGQL } from 'src/generated/graphql';
import { createGroupNodes, GroupNode } from '../../utilities';
import { PageService } from 'src/app/share/page/page.service';

interface FlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

@Component({
  selector: 'app-group-tree',
  templateUrl: './group-tree.component.html'
})
export class GroupTreeComponent implements OnInit, OnDestroy {

  transformer = (node: GroupNode, level: number) => {
    let groupName: string = node.groupName
    if (level === 0) {
      if (node.parentGroupName) {
        groupName = `${node.parentGroupName} / ${node.groupName}`
      }
    }

    return {
      expandable: !!node.children && node.children.length > 0,
      name: groupName,
      level: level,
    };
  }

  treeControl = new FlatTreeControl<FlatNode>(node => node.level, node => node.expandable);;
  treeFlattener = new MatTreeFlattener(this.transformer, node => node.level, node => node.expandable, node => node.children);
  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  dataLoad: Observable<GroupNode[]> | null = null;

  loadDataSubscription: Subscription;

  constructor(private groupsGql: EffectiveGroupsGQL, pageService: PageService) {
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData()
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
  }

  onLoadData() {
    this.dataSource.data = [];
    this.dataLoad = this.groupsGql.fetch()
      .pipe(
        map(res => {
          let [_, rootNodes] = createGroupNodes(res.data.effectiveGroups)
          return rootNodes;
        }),
        share()
      );

    this.dataLoad.subscribe(rootNodes => {
        this.dataSource.data = rootNodes;
        this.treeControl.expandAll();
      });
  }

  hasChild = (_: number, node: FlatNode) => {
    return node.expandable
  }
}
