import { GroupCore, GroupFragment, Role, GroupWithPathFragment } from 'src/generated/graphql';
import { DEFAULT_GROUP_ID } from './constants';

export type RoleMap<R> = {[key in Role]: R};

export function createGroupName(groupName: string, groupPath: GroupCore[]): string {
  return createGroupPathName(groupPath.map(group => group.groupName).concat(groupName));
}

export function createGroupPathNameByGroups(groupPath: GroupCore[]): string {
  return createGroupPathName(groupPath.map(group => group.groupName));
}

export function createGroupPathName(groupNamePath: string[]): string {
  return groupNamePath.reduce((acc, path) => {
    if (acc.length === 0) {
      return path;
    } else {
      return `${acc} / ${path}`;
    }
  }, '');
}

// export function createGroupIdToName(groupsQuery: GroupsQuery): { [key: string]: string }  {
//   let groupToName: { [key: string]: string } = {};
//   groupsQuery.manageableGroupPaths
//     .forEach(path => {
//       path.groupPath
//       .forEach(group => {
//         groupToName[group.groupId] = group.groupName;
//       })
//     });
//   groupsQuery.manageableGroups
//     .forEach(group => {
//       groupToName[group.groupId] = group.groupName;
//     });
//   return groupToName
// }

export interface GroupNode extends GroupFragment {
  parentGroupName?: string;
  // group: GroupFragment;
  children?: GroupNode[];
  root?: boolean;
}

export function createGroupNodes(groupsQuery: GroupWithPathFragment[]): [{[key: string]: GroupNode}, GroupNode[]] {
  // let parentGroupNames: {[key: string]: string } = {}
  // groupsQuery.manageableGroupPaths.forEach(path => {
  //   let groupPath = [...path.groupPath];
  //   groupPath.pop();
  //   if (groupPath.length) {
  //     let parentGroupId = groupPath[groupPath.length - 1].groupId;
  //     parentGroupNames[parentGroupId] = createGroupPathName(groupPath.map(groupPath => groupPath.groupName));
  //   }
  // });

  let groups: {[key: string]: GroupNode} = {}
  groupsQuery.forEach(group => {
    groups[group.group.groupId] = Object.assign({
      parentGroupName: createGroupPathName(group.path.map(groupPath => groupPath.groupName))
    }, group.group);
  });

  let rootNodes: GroupNode[] = [];

  Object.values(groups).forEach(node => {
    let parentGroupId = node.parentGroupId
    if(!!parentGroupId) {
      let parentNode = groups[parentGroupId];
      // 管理対象グループが、別の管理グループの子グループの場合があるので、先に子グループとして登録を試みる
      if (parentNode) {
        let children = parentNode.children || [];
        parentNode.children = children.concat(node);
      } else {
        node.root = true;
        rootNodes.push(node);
      }
    }
  })
  if (rootNodes.length === 0) {
    rootNodes.push(groups[DEFAULT_GROUP_ID]);
  }

  return [groups, rootNodes];

}


export const roleDefine: RoleMap<{ name: string, order: number }>
= {
  ADMIN: { name: 'システム管理者', order: 0},
  GROUP: { name: 'グループ管理者', order: 1},
  SLIDE: { name: '教材管理者', order: 2},
  LESSON: { name: '講座管理者', order: 3},
  STUDY: { name: '受講者', order: 4},
  NONE: { name: 'なし', order: 5}
}

export function createRoleName(roles: Role[]): string {
  return roles
    .sort((a, b) => roleDefine[a].order - roleDefine[b].order)
    .map(role => roleDefine[role].name)
    .join(' / ')
}

