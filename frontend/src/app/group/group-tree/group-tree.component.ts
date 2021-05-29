import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { Observable, Subscription } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { EffectiveGroupsGQL, Role } from 'src/generated/graphql';
import { createGroupNodes, GroupNode } from '../../utilities';

interface FlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

@Component({
  selector: 'app-group-tree',
  templateUrl: './group-tree.component.html'
})
export class GroupTreeComponent implements OnInit {

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

  // loadDataSubscription: Subscription;

  constructor(private groupsGql: EffectiveGroupsGQL) {
    // this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData()
  }

  // ngOnDestroy(): void {
  //   this.loadDataSubscription.unsubscribe();
  // }

  onLoadData = (): void => {
    this.dataSource.data = [];
    this.dataLoad = this.groupsGql.fetch({role: Role.Group})
      .pipe(
        map(res => {
          let [_, rootNodes] = createGroupNodes(res.data.effectiveGroupsByUser)
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
