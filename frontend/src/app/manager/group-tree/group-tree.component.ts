import { FlatTreeControl, NestedTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener, MatTreeNestedDataSource } from '@angular/material/tree';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DEFAULT_GROUP_ID } from 'src/app/constants';
import { GroupFragment, GroupsGQL } from 'src/generated/graphql';

interface GroupNode {
  group: GroupFragment
  children?: GroupNode[];
}

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
  // treeControl = new NestedTreeControl<GroupNode>(node => node.children);;
  // dataSource = new MatTreeNestedDataSource<GroupNode>();

  transformer = (node: GroupNode, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.group.groupName,
      level: level,
    };
  }

  treeControl = new FlatTreeControl<FlatNode>(node => node.level, node => node.expandable);;
  treeFlattener = new MatTreeFlattener(this.transformer, node => node.level, node => node.expandable, node => node.children);
  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  dataLoad: Observable<GroupNode> | null = null;

  constructor(private groupsGql: GroupsGQL) {
  }

  ngOnInit(): void {
    this.onLoadData()
  }

  onLoadData() {
    this.dataSource.data = [];
    this.dataLoad = this.groupsGql.fetch()
      .pipe(map(res => {
        let map: {[key: string]: GroupNode} = {}
        res.data.groups.forEach(group => {
          map[group.groupId] = { group }
        });

        Object.values(map).forEach(node => {
          if(!!node.group.parentGroupId) {
            let parentNode = map[node.group.parentGroupId];
            let children = parentNode.children || []
            parentNode.children = children.concat(node);
          }
        })
        return map[DEFAULT_GROUP_ID];
      }));

    this.dataLoad.subscribe(rootNode => {
        this.dataSource.data = [rootNode];
        this.treeControl.expandAll();
      });
  }

  hasChild = (_: number, node: FlatNode) => {
    return node.expandable
  }
}
