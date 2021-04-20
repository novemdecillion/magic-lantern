import { NestedTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { finalize } from 'rxjs/operators';
import { DEFAULT_GROUP_ID } from 'src/app/constants';
import { GroupFragment, GroupsGQL } from 'src/generated/graphql';

interface GroupNode {
  group: GroupFragment
  children?: GroupNode[];
}

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html'
})
export class GroupListComponent implements OnInit {
  treeControl = new NestedTreeControl<GroupNode>(node => node.children);;
  dataSource = new MatTreeNestedDataSource<GroupNode>();
  isLoading = false;

  constructor(private groupsGql: GroupsGQL) {
  }

  ngOnInit(): void {
    this.onLoadData()
  }

  onLoadData() {
    this.dataSource.data = [];
    this.isLoading = true;
    this.groupsGql.fetch()
      .pipe(finalize(() => this.isLoading = false))
      .subscribe(res => {
        let map: {[key: string]: GroupNode} = {}
        res.data.groups.forEach(group => {
          map[group.groupId] = { group }
        });

        Object.values(map).forEach(node => {
          if(!!node.group.parentGroupTransitionId) {
            let parentNode = map[node.group.parentGroupTransitionId];
            let children = parentNode.children || []
            parentNode.children = children.concat(node);
          }
        })
        let rootNode = map[DEFAULT_GROUP_ID];
        this.dataSource.data = [rootNode];
      });

  }

  hasChild = (_: number, node: GroupNode) => !!node.children && 0 < node.children.length
}
