import parseISO from 'date-fns/parseISO';
import { AuthorityFragment, GroupCore, GroupFragment, NoticeFragment, Role, SlideFragment, StudyFragment, StudyStatus } from 'src/generated/graphql';
import { DEFAULT_GROUP_ID } from './constants';
import { ExecutionResult } from 'graphql';
import { MatSnackBar } from '@angular/material/snack-bar';

export function logout() {
  location.href = '/login';
}

export function parseISOIfExist(dateTime?: string): Date | null {
  if (dateTime) {
    return parseISO(dateTime);
  }
  return null;
}

export function sortNotices(notices: NoticeFragment[]): NoticeFragment[] {
  return notices.sort((a, b) => {
    if(a.updateAt < b.updateAt) {
      return 1;
    } else if(a.updateAt == b.updateAt) {
      return 0;
    } else {
      return -1;
    }
  });
}

export function hasRootGroupAuthority(auths: AuthorityFragment[]): boolean {
  return auths.find(auth => auth.groupId == DEFAULT_GROUP_ID)?.roles?.includes(Role.Group) == true
}

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

export interface IGroupNode {
  parentGroupName?: string;
  children?: GroupNode[];
  root?: boolean;
}

export type GroupNode = IGroupNode & GroupFragment;


export function createGroupNodes<T extends GroupFragment>(groupsQuery: T[]): [{[key: string]: (T & IGroupNode)}, (T & IGroupNode)[]] {
  type GroupNodeWithT = T & IGroupNode;

  let groups: {[key: string]: GroupNodeWithT} = {}
  groupsQuery.forEach(group => {
    groups[group.groupId] = Object.assign({
      parentGroupName: createGroupPathName(group.path.map(groupPath => groupPath.groupName))
    }, group);
  });

  let rootNodes: (GroupNodeWithT)[] = [];

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
  ADMIN: { name: 'システム', order: 0},
  GROUP: { name: 'グループ', order: 1},
  SLIDE: { name: '教材', order: 2},
  LESSON: { name: '講座', order: 3},
  STUDY: { name: '受講', order: 4}
}

export function createRoleName(roles?: Role[] | null): string {
  if (!roles) {
    return 'なし';
  }
  return roles
    .sort((a, b) => roleDefine[a].order - roleDefine[b].order)
    .map(role => roleDefine[role].name)
    .join(' / ')
}

export function toBlob(base64: string, mime: string): Blob | null {
  let bin = atob(base64.replace(/^.*,/, ''));
  let buffer = new Uint8Array(bin.length);
  for (var i = 0; i < bin.length; i++) {
      buffer[i] = bin.charCodeAt(i);
  }
  // Blobを作成
  try {
    let blob = new Blob([buffer.buffer], {
      type: mime
    });
    return blob;
  } catch (e) {
    return null;
  }
}

export function downloadBlob(filename: string, blob: Blob, element: HTMLElement) {
  let url = window.URL.createObjectURL(blob);
  let a = document.createElement('a');
  element.appendChild(a);

  a.setAttribute('style', 'display: none');
  a.href = url;
  a.download = filename;
  a.click();

  element.removeChild(a);
  window.URL.revokeObjectURL(url);
}


export function studyStatus(study: StudyFragment): string {
  switch(study.status) {
    case StudyStatus.NotStart:
      return '未着手';
    case StudyStatus.OnGoing:
      return `実施中(${study.progressRate}%)`;
    case StudyStatus.Pass:
      let sumPass = studyScore(study)
      return `合格(得点:${sumPass[0]}点 / 合格:${sumPass[1]}点)`;
    case StudyStatus.Failed:
      let sumFailed = studyScore(study)
      return `不合格(得点:${sumFailed[0]}点 / 合格:${sumFailed[1]}点)`;
    case StudyStatus.Excluded:
      return '対象外';
  }
}

export function studyScore(study: StudyFragment): [number, number, number] {
  return study.scoreDetails.reduce((sum, chapter)=> {
    let sumQuestions = chapter.questions.reduce((prevSum, question)=>{
      return [prevSum[0] + question.scoring, prevSum[1] + question.score];
    }, [0, 0]);
    return [sum[0] + sumQuestions[0], sum[1] + chapter.passScore, sum[1] + sumQuestions[1]];
  }, [0, 0, 0]);
}

export interface StudyStatusRecord {
  chapterIndex: number;
  title: string;
  status: string;
  pages: number
  numberOfPages: number;

  score?: number;
  passScore?: number;
  totalScore?: number;
}

export function convertToStudyStatus(slide: SlideFragment, study?: StudyFragment): StudyStatusRecord[] {
  let records = slide.config.chapters
    ?.map((chapter, index) => {
      let record: StudyStatusRecord = {
        chapterIndex: index,
        title: chapter.title,
        status: '未着手',
        pages: 0,
        numberOfPages: chapter.numberOfPages
      };
      if (chapter.__typename == 'ExamChapter') {
        record.passScore = chapter.passScore ?? 0;
        record.totalScore = chapter.questions.reduce((acc, value) => acc + (value.score ?? 0), 0);
      }
      return record
    })
    ?? [];

  if (study) {
    study.progressDetails.forEach(prog => {
      records[prog.chapterIndex].pages = prog.pageIndexes.length;
    })
    study.scoreDetails.forEach(score => {
      records[score.chapterIndex].score = score.questions.reduce((acc, value) => acc + (value.scoring ?? 0), 0);
    })
  }

  records
    .forEach(record => {
      if ((undefined != record.score)
          && (undefined != record.passScore)
          && (undefined != record.totalScore)) {
        if (record.passScore <= record.score) {
          record.status = `合格 (得点:${record.score}点 / 合格:${record.passScore}点)`
        } else {
          record.status = `不合格 (得点:${record.score}点 / 合格:${record.passScore}点)`
        }
      } else if (record.pages == record.numberOfPages) {
        record.status = '済';
      } else if (0 < record.pages) {
        record.status = '実施中';
      }
    });
  return records;
}

export function errorMessageIfNeed(result: ExecutionResult, snackBar: MatSnackBar, force?: boolean): boolean {
  let errorMessage = result
    .errors
    ?.filter(error => error.extensions?.type == 'ApiException')
    ?.map(error => error.message)
    ?.join('\n')

  if (errorMessage || force) {
    if (!errorMessage) {
      errorMessage = '処理に失敗しました。';
    }
    snackBar.open(errorMessage, '確認');
    return true;
  }
  return false
}
