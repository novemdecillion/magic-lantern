import { gql } from 'apollo-angular';
import { Injectable } from '@angular/core';
import * as Apollo from 'apollo-angular';
export type Maybe<T> = T | null;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
  DateTime: any;
  Date: any;
  Time: any;
  Upload: any;
};







export type Query = {
  __typename?: 'Query';
  currentUser: User;
  users: Array<User>;
  userCount: Scalars['Int'];
  realms: Array<Realm>;
  notices: Array<Notice>;
  effectiveNotices: Array<Notice>;
  slides: Array<Slide>;
  manageableLessons: Array<Lesson>;
  myStudies: Array<Study>;
  authoritativeGroups: Array<Group>;
  effectiveGroups: Array<Group>;
  group?: Maybe<Group>;
  isTopManageableGroup: Scalars['Boolean'];
  groupMembers: Array<User>;
  groupAppendableMembers: Array<User>;
};


export type QueryAuthoritativeGroupsArgs = {
  role: Role;
};


export type QueryEffectiveGroupsArgs = {
  role: Role;
};


export type QueryGroupArgs = {
  groupId: Scalars['ID'];
};


export type QueryIsTopManageableGroupArgs = {
  groupId: Scalars['ID'];
};


export type QueryGroupMembersArgs = {
  groupId: Scalars['ID'];
};


export type QueryGroupAppendableMembersArgs = {
  groupId?: Maybe<Scalars['ID']>;
};

export type Mutation = {
  __typename?: 'Mutation';
  syncRealm?: Maybe<Scalars['Boolean']>;
  addUser?: Maybe<AddUserResult>;
  updateUser?: Maybe<UpdateUserResult>;
  changePassword?: Maybe<ChangePasswordResult>;
  deleteUser?: Maybe<Scalars['Boolean']>;
  addNotice?: Maybe<Scalars['Boolean']>;
  updateNotice?: Maybe<Scalars['Boolean']>;
  deleteNotice?: Maybe<Scalars['Boolean']>;
  addGroup?: Maybe<Scalars['Boolean']>;
  updateGroup?: Maybe<Scalars['Boolean']>;
  deleteGroup?: Maybe<Scalars['Boolean']>;
  addGroupMember?: Maybe<Scalars['Boolean']>;
  updateGroupMember?: Maybe<Scalars['Boolean']>;
  deleteGroupMember?: Maybe<Scalars['Boolean']>;
  addSlide?: Maybe<Scalars['Boolean']>;
  deleteSlide?: Maybe<Scalars['Boolean']>;
  enableSlide?: Maybe<Scalars['Boolean']>;
  addLesson?: Maybe<Scalars['Boolean']>;
  deleteLesson?: Maybe<Scalars['Boolean']>;
};


export type MutationSyncRealmArgs = {
  realmId?: Maybe<Scalars['String']>;
};


export type MutationAddUserArgs = {
  command: AddUserCommand;
};


export type MutationUpdateUserArgs = {
  command: UpdateUserCommand;
};


export type MutationChangePasswordArgs = {
  command: ChangePasswordCommand;
};


export type MutationDeleteUserArgs = {
  userId: Scalars['ID'];
};


export type MutationAddNoticeArgs = {
  command: AddNoticeCommand;
};


export type MutationUpdateNoticeArgs = {
  command: UpdateNoticeCommand;
};


export type MutationDeleteNoticeArgs = {
  noticeId: Scalars['ID'];
};


export type MutationAddGroupArgs = {
  command: AddGroupCommand;
};


export type MutationUpdateGroupArgs = {
  command: UpdateGroupCommand;
};


export type MutationDeleteGroupArgs = {
  groupId: Scalars['ID'];
};


export type MutationAddGroupMemberArgs = {
  command: GroupMemberCommand;
};


export type MutationUpdateGroupMemberArgs = {
  command: GroupMemberCommand;
};


export type MutationDeleteGroupMemberArgs = {
  command: DeleteGroupMemberCommand;
};


export type MutationAddSlideArgs = {
  command: AddSlideCommand;
};


export type MutationDeleteSlideArgs = {
  slideId: Scalars['ID'];
};


export type MutationEnableSlideArgs = {
  slideId: Scalars['ID'];
  enable: Scalars['Boolean'];
};


export type MutationAddLessonArgs = {
  command: AddLessonCommand;
};


export type MutationDeleteLessonArgs = {
  lessonId: Scalars['ID'];
};

export type Member = {
  __typename?: 'Member';
  userId: Scalars['ID'];
  roles: Array<Role>;
};

export type IGroupCOre = {
  groupId: Scalars['ID'];
  groupName: Scalars['String'];
};

export type GroupCore = IGroupCOre & {
  __typename?: 'GroupCore';
  groupId: Scalars['ID'];
  groupName: Scalars['String'];
};

export type Group = IGroupCOre & {
  __typename?: 'Group';
  groupId: Scalars['ID'];
  groupOriginId: Scalars['ID'];
  groupGenerationId: Scalars['ID'];
  groupName: Scalars['String'];
  parentGroupId?: Maybe<Scalars['ID']>;
  path: Array<GroupCore>;
};

export type AddGroupCommand = {
  groupOriginId?: Maybe<Scalars['ID']>;
  groupGenerationId?: Maybe<Scalars['ID']>;
  groupName: Scalars['String'];
  parentGroupId: Scalars['ID'];
};

export type UpdateGroupCommand = {
  groupId: Scalars['ID'];
  groupName: Scalars['String'];
};

export type GroupMemberCommand = {
  groupId: Scalars['ID'];
  userIds: Array<Scalars['ID']>;
  role: Array<Role>;
};

export type DeleteGroupMemberCommand = {
  groupId: Scalars['ID'];
  userIds: Array<Scalars['ID']>;
};

export type Lesson = {
  __typename?: 'Lesson';
  lessonId: Scalars['ID'];
  groupId: Scalars['ID'];
  slideId: Scalars['ID'];
  group: Group;
  slide: Slide;
};

export type AddLessonCommand = {
  slideId: Scalars['ID'];
  groupIds: Array<Scalars['ID']>;
};

export type Notice = {
  __typename?: 'Notice';
  noticeId: Scalars['ID'];
  message: Scalars['String'];
  startAt?: Maybe<Scalars['Date']>;
  endAt?: Maybe<Scalars['Date']>;
  updateAt: Scalars['DateTime'];
};

export type AddNoticeCommand = {
  message: Scalars['String'];
  startAt?: Maybe<Scalars['Date']>;
  endAt?: Maybe<Scalars['Date']>;
};

export type UpdateNoticeCommand = {
  noticeId: Scalars['ID'];
  message: Scalars['String'];
  startAt?: Maybe<Scalars['Date']>;
  endAt?: Maybe<Scalars['Date']>;
};

export type Chapter = {
  __typename?: 'Chapter';
  title: Scalars['String'];
};

export type SlideConfig = {
  __typename?: 'SlideConfig';
  title: Scalars['String'];
  chapters?: Maybe<Array<Chapter>>;
};

export type Slide = {
  __typename?: 'Slide';
  slideId: Scalars['ID'];
  enable: Scalars['Boolean'];
  config: SlideConfig;
};

export type AddSlideCommand = {
  slideId: Scalars['ID'];
  slideFile: Scalars['Upload'];
};

export enum Role {
  Admin = 'ADMIN',
  Group = 'GROUP',
  Slide = 'SLIDE',
  Lesson = 'LESSON',
  Study = 'STUDY',
  None = 'NONE'
}

export type Authority = {
  __typename?: 'Authority';
  groupId: Scalars['ID'];
  roles: Array<Role>;
};

export type User = {
  __typename?: 'User';
  userId: Scalars['ID'];
  accountName: Scalars['String'];
  userName: Scalars['String'];
  email?: Maybe<Scalars['String']>;
  realmId: Scalars['String'];
  enabled: Scalars['Boolean'];
  authorities: Array<Authority>;
  isSystemRealm?: Maybe<Scalars['Boolean']>;
};

export type AddUserCommand = {
  accountName: Scalars['String'];
  userName: Scalars['String'];
  password: Scalars['String'];
  email?: Maybe<Scalars['String']>;
  enabled: Scalars['Boolean'];
  isAdmin: Scalars['Boolean'];
  isGroupManager: Scalars['Boolean'];
};

export enum AddUserResult {
  Success = 'Success',
  DuplicateAccountName = 'DuplicateAccountName',
  UnknownError = 'UnknownError'
}

export type UpdateUserCommand = {
  userId: Scalars['ID'];
  accountName?: Maybe<Scalars['String']>;
  userName?: Maybe<Scalars['String']>;
  password?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  enabled?: Maybe<Scalars['Boolean']>;
  isAdmin: Scalars['Boolean'];
  isGroupManager: Scalars['Boolean'];
};

export enum UpdateUserResult {
  Success = 'Success',
  UnknownError = 'UnknownError'
}

export type ChangePasswordCommand = {
  oldPassword: Scalars['String'];
  newPassword: Scalars['String'];
};

export enum ChangePasswordResult {
  Success = 'Success',
  UserNotFound = 'UserNotFound',
  PasswordNotMatch = 'PasswordNotMatch',
  UnknownError = 'UnknownError'
}

export type Realm = {
  __typename?: 'Realm';
  realmId: Scalars['String'];
  realmName: Scalars['String'];
  enabled: Scalars['Boolean'];
  syncAt?: Maybe<Scalars['DateTime']>;
};

export enum StudyStatus {
  NotStart = 'NOT_START',
  OnGoing = 'ON_GOING',
  Pass = 'PASS',
  Failed = 'FAILED'
}

export type StudyQuestionRecord = {
  __typename?: 'StudyQuestionRecord';
  score: Scalars['Int'];
  scoring: Scalars['Int'];
};

export type StudyChapterRecord = {
  __typename?: 'StudyChapterRecord';
  chapterIndex: Scalars['Int'];
  passScore: Scalars['Int'];
  questions: Array<StudyQuestionRecord>;
};

export type StudyQuestionAnswer = {
  __typename?: 'StudyQuestionAnswer';
  questionIndex: Scalars['Int'];
  answers: Array<Scalars['Int']>;
};

export type StudyChapterAnswer = {
  __typename?: 'StudyChapterAnswer';
  chapterIndex: Scalars['Int'];
  questions: Array<StudyQuestionAnswer>;
};

export type StudyProgress = {
  __typename?: 'StudyProgress';
  chapterIndex: Scalars['Int'];
  pageIndexes: Array<Scalars['Int']>;
};

export type Study = {
  __typename?: 'Study';
  studyId?: Maybe<Scalars['ID']>;
  userId: Scalars['ID'];
  slideId: Scalars['ID'];
  status: StudyStatus;
  progressDetails: Array<StudyProgress>;
  progressRate?: Maybe<Scalars['Int']>;
  answerDetails: Array<StudyChapterAnswer>;
  scoreDetails: Array<StudyChapterRecord>;
  startAt?: Maybe<Scalars['DateTime']>;
  endAt?: Maybe<Scalars['DateTime']>;
  slide: Slide;
  lessons: Array<Lesson>;
};

export type ForMemberListQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type ForMemberListQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'isTopManageableGroup'>
  & { group?: Maybe<(
    { __typename?: 'Group' }
    & GroupFragment
  )>, groupMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type ForEditMemberQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type ForEditMemberQuery = (
  { __typename?: 'Query' }
  & { group?: Maybe<(
    { __typename?: 'Group' }
    & GroupFragment
  )>, groupMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type ForAddMemberQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type ForAddMemberQuery = (
  { __typename?: 'Query' }
  & { group?: Maybe<(
    { __typename?: 'Group' }
    & GroupFragment
  )>, groupAppendableMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type GroupCoreFragment = (
  { __typename?: 'GroupCore' }
  & Pick<GroupCore, 'groupId' | 'groupName'>
);

export type GroupFragment = (
  { __typename?: 'Group' }
  & Pick<Group, 'groupId' | 'groupOriginId' | 'groupGenerationId' | 'groupName' | 'parentGroupId'>
  & { path: Array<(
    { __typename?: 'GroupCore' }
    & GroupCoreFragment
  )> }
);

export type GroupQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type GroupQuery = (
  { __typename?: 'Query' }
  & { group?: Maybe<(
    { __typename?: 'Group' }
    & GroupFragment
  )> }
);

export type AuthoritativeGroupsQueryVariables = Exact<{
  role: Role;
}>;


export type AuthoritativeGroupsQuery = (
  { __typename?: 'Query' }
  & { authoritativeGroups: Array<(
    { __typename?: 'Group' }
    & GroupFragment
  )> }
);

export type EffectiveGroupsQueryVariables = Exact<{
  role: Role;
}>;


export type EffectiveGroupsQuery = (
  { __typename?: 'Query' }
  & { effectiveGroups: Array<(
    { __typename?: 'Group' }
    & GroupFragment
  )> }
);

export type AddGroupMutationVariables = Exact<{
  command: AddGroupCommand;
}>;


export type AddGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addGroup'>
);

export type UpdateGroupMutationVariables = Exact<{
  command: UpdateGroupCommand;
}>;


export type UpdateGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'updateGroup'>
);

export type DeleteGroupMutationVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type DeleteGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteGroup'>
);

export type AddGroupMemberMutationVariables = Exact<{
  command: GroupMemberCommand;
}>;


export type AddGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addGroupMember'>
);

export type UpdateGroupMemberMutationVariables = Exact<{
  command: GroupMemberCommand;
}>;


export type UpdateGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'updateGroupMember'>
);

export type DeleteGroupMemberMutationVariables = Exact<{
  command: DeleteGroupMemberCommand;
}>;


export type DeleteGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteGroupMember'>
);

export type LessonFragment = (
  { __typename?: 'Lesson' }
  & Pick<Lesson, 'lessonId' | 'groupId' | 'slideId'>
  & { group: (
    { __typename?: 'Group' }
    & GroupFragment
  ), slide: (
    { __typename?: 'Slide' }
    & SlideFragment
  ) }
);

export type ManageableLessonsQueryVariables = Exact<{ [key: string]: never; }>;


export type ManageableLessonsQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'Lesson' }
    & LessonFragment
  )> }
);

export type PrepareAddLessonQueryVariables = Exact<{ [key: string]: never; }>;


export type PrepareAddLessonQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'lessonId' | 'groupId' | 'slideId'>
  )>, authoritativeGroups: Array<(
    { __typename?: 'Group' }
    & GroupFragment
  )>, slides: Array<(
    { __typename?: 'Slide' }
    & SlideFragment
  )> }
);

export type AddLessonMutationVariables = Exact<{
  command: AddLessonCommand;
}>;


export type AddLessonMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addLesson'>
);

export type DeleteLessonMutationVariables = Exact<{
  lessonId: Scalars['ID'];
}>;


export type DeleteLessonMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteLesson'>
);

export type NoticeFragment = (
  { __typename?: 'Notice' }
  & Pick<Notice, 'noticeId' | 'message' | 'startAt' | 'endAt' | 'updateAt'>
);

export type NoticesQueryVariables = Exact<{ [key: string]: never; }>;


export type NoticesQuery = (
  { __typename?: 'Query' }
  & { notices: Array<(
    { __typename?: 'Notice' }
    & NoticeFragment
  )> }
);

export type EffectiveNoticesQueryVariables = Exact<{ [key: string]: never; }>;


export type EffectiveNoticesQuery = (
  { __typename?: 'Query' }
  & { effectiveNotices: Array<(
    { __typename?: 'Notice' }
    & NoticeFragment
  )> }
);

export type AddNoticeMutationVariables = Exact<{
  command: AddNoticeCommand;
}>;


export type AddNoticeMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addNotice'>
);

export type UpdateNoticeMutationVariables = Exact<{
  command: UpdateNoticeCommand;
}>;


export type UpdateNoticeMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'updateNotice'>
);

export type DeleteNoticeMutationVariables = Exact<{
  noticeId: Scalars['ID'];
}>;


export type DeleteNoticeMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteNotice'>
);

export type SlideConfigFragment = (
  { __typename?: 'SlideConfig' }
  & Pick<SlideConfig, 'title'>
  & { chapters?: Maybe<Array<(
    { __typename?: 'Chapter' }
    & Pick<Chapter, 'title'>
  )>> }
);

export type SlideFragment = (
  { __typename?: 'Slide' }
  & Pick<Slide, 'slideId' | 'enable'>
  & { config: (
    { __typename?: 'SlideConfig' }
    & SlideConfigFragment
  ) }
);

export type SlidesQueryVariables = Exact<{ [key: string]: never; }>;


export type SlidesQuery = (
  { __typename?: 'Query' }
  & { slides: Array<(
    { __typename?: 'Slide' }
    & SlideFragment
  )> }
);

export type AddSlideMutationVariables = Exact<{
  command: AddSlideCommand;
}>;


export type AddSlideMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addSlide'>
);

export type DeleteSlideMutationVariables = Exact<{
  slideId: Scalars['ID'];
}>;


export type DeleteSlideMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteSlide'>
);

export type AuthorityFragment = (
  { __typename?: 'Authority' }
  & Pick<Authority, 'groupId' | 'roles'>
);

export type UserFragment = (
  { __typename?: 'User' }
  & Pick<User, 'userId' | 'accountName' | 'userName' | 'realmId' | 'email' | 'enabled' | 'isSystemRealm'>
  & { authorities: Array<(
    { __typename?: 'Authority' }
    & AuthorityFragment
  )> }
);

export type RealmFragment = (
  { __typename?: 'Realm' }
  & Pick<Realm, 'realmId' | 'realmName' | 'enabled' | 'syncAt'>
);

export type CurrentUserQueryVariables = Exact<{ [key: string]: never; }>;


export type CurrentUserQuery = (
  { __typename?: 'Query' }
  & { currentUser: (
    { __typename?: 'User' }
    & UserFragment
  ) }
);

export type UsersQueryVariables = Exact<{ [key: string]: never; }>;


export type UsersQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'userCount'>
  & { users: Array<(
    { __typename?: 'User' }
    & UserFragment
  )>, realms: Array<(
    { __typename?: 'Realm' }
    & RealmFragment
  )> }
);

export type RealmsQueryVariables = Exact<{ [key: string]: never; }>;


export type RealmsQuery = (
  { __typename?: 'Query' }
  & { realms: Array<(
    { __typename?: 'Realm' }
    & RealmFragment
  )> }
);

export type GroupAppendableMembersQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type GroupAppendableMembersQuery = (
  { __typename?: 'Query' }
  & { groupAppendableMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type SyncRealmMutationVariables = Exact<{
  realmId?: Maybe<Scalars['String']>;
}>;


export type SyncRealmMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'syncRealm'>
);

export type StudyChapterRecordFragment = (
  { __typename?: 'StudyChapterRecord' }
  & Pick<StudyChapterRecord, 'chapterIndex' | 'passScore'>
  & { questions: Array<(
    { __typename?: 'StudyQuestionRecord' }
    & Pick<StudyQuestionRecord, 'score' | 'scoring'>
  )> }
);

export type StudyProgressFragment = (
  { __typename?: 'StudyProgress' }
  & Pick<StudyProgress, 'chapterIndex' | 'pageIndexes'>
);

export type StudyChapterAnswerFragment = (
  { __typename?: 'StudyChapterAnswer' }
  & Pick<StudyChapterAnswer, 'chapterIndex'>
  & { questions: Array<(
    { __typename?: 'StudyQuestionAnswer' }
    & Pick<StudyQuestionAnswer, 'questionIndex' | 'answers'>
  )> }
);

export type StudyFragment = (
  { __typename?: 'Study' }
  & Pick<Study, 'studyId' | 'userId' | 'status' | 'progressRate' | 'startAt' | 'endAt'>
  & { progressDetails: Array<(
    { __typename?: 'StudyProgress' }
    & StudyProgressFragment
  )>, answerDetails: Array<(
    { __typename?: 'StudyChapterAnswer' }
    & StudyChapterAnswerFragment
  )>, scoreDetails: Array<(
    { __typename?: 'StudyChapterRecord' }
    & StudyChapterRecordFragment
  )>, slide: (
    { __typename?: 'Slide' }
    & SlideFragment
  ), lessons: Array<(
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'lessonId'>
    & { group: (
      { __typename?: 'Group' }
      & GroupFragment
    ) }
  )> }
);

export type MyStudiesQueryVariables = Exact<{ [key: string]: never; }>;


export type MyStudiesQuery = (
  { __typename?: 'Query' }
  & { myStudies: Array<(
    { __typename?: 'Study' }
    & StudyFragment
  )> }
);

export type AddUserMutationVariables = Exact<{
  command: AddUserCommand;
}>;


export type AddUserMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addUser'>
);

export type UpdateUserMutationVariables = Exact<{
  command: UpdateUserCommand;
}>;


export type UpdateUserMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'updateUser'>
);

export type ChangePasswordMutationVariables = Exact<{
  command: ChangePasswordCommand;
}>;


export type ChangePasswordMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'changePassword'>
);

export type DeleteUserMutationVariables = Exact<{
  userId: Scalars['ID'];
}>;


export type DeleteUserMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteUser'>
);

export const GroupCoreFragmentDoc = gql`
    fragment groupCore on GroupCore {
  groupId
  groupName
}
    `;
export const GroupFragmentDoc = gql`
    fragment group on Group {
  groupId
  groupOriginId
  groupGenerationId
  groupName
  parentGroupId
  path {
    ...groupCore
  }
}
    ${GroupCoreFragmentDoc}`;
export const SlideConfigFragmentDoc = gql`
    fragment slideConfig on SlideConfig {
  title
  chapters {
    title
  }
}
    `;
export const SlideFragmentDoc = gql`
    fragment slide on Slide {
  slideId
  enable
  config {
    ...slideConfig
  }
}
    ${SlideConfigFragmentDoc}`;
export const LessonFragmentDoc = gql`
    fragment lesson on Lesson {
  lessonId
  groupId
  slideId
  group {
    ...group
  }
  slide {
    ...slide
  }
}
    ${GroupFragmentDoc}
${SlideFragmentDoc}`;
export const NoticeFragmentDoc = gql`
    fragment notice on Notice {
  noticeId
  message
  startAt
  endAt
  updateAt
}
    `;
export const AuthorityFragmentDoc = gql`
    fragment authority on Authority {
  groupId
  roles
}
    `;
export const UserFragmentDoc = gql`
    fragment user on User {
  userId
  accountName
  userName
  realmId
  email
  enabled
  isSystemRealm
  authorities {
    ...authority
  }
}
    ${AuthorityFragmentDoc}`;
export const RealmFragmentDoc = gql`
    fragment realm on Realm {
  realmId
  realmName
  enabled
  syncAt
}
    `;
export const StudyProgressFragmentDoc = gql`
    fragment studyProgress on StudyProgress {
  chapterIndex
  pageIndexes
}
    `;
export const StudyChapterAnswerFragmentDoc = gql`
    fragment studyChapterAnswer on StudyChapterAnswer {
  chapterIndex
  questions {
    questionIndex
    answers
  }
}
    `;
export const StudyChapterRecordFragmentDoc = gql`
    fragment studyChapterRecord on StudyChapterRecord {
  chapterIndex
  passScore
  questions {
    score
    scoring
  }
}
    `;
export const StudyFragmentDoc = gql`
    fragment study on Study {
  studyId
  userId
  status
  progressDetails {
    ...studyProgress
  }
  progressRate
  answerDetails {
    ...studyChapterAnswer
  }
  scoreDetails {
    ...studyChapterRecord
  }
  startAt
  endAt
  slide {
    ...slide
  }
  lessons {
    lessonId
    group {
      ...group
    }
  }
}
    ${StudyProgressFragmentDoc}
${StudyChapterAnswerFragmentDoc}
${StudyChapterRecordFragmentDoc}
${SlideFragmentDoc}
${GroupFragmentDoc}`;
export const ForMemberListDocument = gql`
    query forMemberList($groupId: ID!) {
  group(groupId: $groupId) {
    ...group
  }
  isTopManageableGroup(groupId: $groupId)
  groupMembers(groupId: $groupId) {
    ...user
  }
}
    ${GroupFragmentDoc}
${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForMemberListGQL extends Apollo.Query<ForMemberListQuery, ForMemberListQueryVariables> {
    document = ForMemberListDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ForEditMemberDocument = gql`
    query forEditMember($groupId: ID!) {
  group(groupId: $groupId) {
    ...group
  }
  groupMembers(groupId: $groupId) {
    ...user
  }
}
    ${GroupFragmentDoc}
${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForEditMemberGQL extends Apollo.Query<ForEditMemberQuery, ForEditMemberQueryVariables> {
    document = ForEditMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ForAddMemberDocument = gql`
    query forAddMember($groupId: ID!) {
  group(groupId: $groupId) {
    ...group
  }
  groupAppendableMembers(groupId: $groupId) {
    ...user
  }
}
    ${GroupFragmentDoc}
${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForAddMemberGQL extends Apollo.Query<ForAddMemberQuery, ForAddMemberQueryVariables> {
    document = ForAddMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const GroupDocument = gql`
    query group($groupId: ID!) {
  group(groupId: $groupId) {
    ...group
  }
}
    ${GroupFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class GroupGQL extends Apollo.Query<GroupQuery, GroupQueryVariables> {
    document = GroupDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AuthoritativeGroupsDocument = gql`
    query authoritativeGroups($role: Role!) {
  authoritativeGroups(role: $role) {
    ...group
  }
}
    ${GroupFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class AuthoritativeGroupsGQL extends Apollo.Query<AuthoritativeGroupsQuery, AuthoritativeGroupsQueryVariables> {
    document = AuthoritativeGroupsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const EffectiveGroupsDocument = gql`
    query effectiveGroups($role: Role!) {
  effectiveGroups(role: $role) {
    ...group
  }
}
    ${GroupFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class EffectiveGroupsGQL extends Apollo.Query<EffectiveGroupsQuery, EffectiveGroupsQueryVariables> {
    document = EffectiveGroupsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddGroupDocument = gql`
    mutation addGroup($command: AddGroupCommand!) {
  addGroup(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddGroupGQL extends Apollo.Mutation<AddGroupMutation, AddGroupMutationVariables> {
    document = AddGroupDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const UpdateGroupDocument = gql`
    mutation updateGroup($command: UpdateGroupCommand!) {
  updateGroup(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class UpdateGroupGQL extends Apollo.Mutation<UpdateGroupMutation, UpdateGroupMutationVariables> {
    document = UpdateGroupDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteGroupDocument = gql`
    mutation deleteGroup($groupId: ID!) {
  deleteGroup(groupId: $groupId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteGroupGQL extends Apollo.Mutation<DeleteGroupMutation, DeleteGroupMutationVariables> {
    document = DeleteGroupDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddGroupMemberDocument = gql`
    mutation addGroupMember($command: GroupMemberCommand!) {
  addGroupMember(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddGroupMemberGQL extends Apollo.Mutation<AddGroupMemberMutation, AddGroupMemberMutationVariables> {
    document = AddGroupMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const UpdateGroupMemberDocument = gql`
    mutation updateGroupMember($command: GroupMemberCommand!) {
  updateGroupMember(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class UpdateGroupMemberGQL extends Apollo.Mutation<UpdateGroupMemberMutation, UpdateGroupMemberMutationVariables> {
    document = UpdateGroupMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteGroupMemberDocument = gql`
    mutation deleteGroupMember($command: DeleteGroupMemberCommand!) {
  deleteGroupMember(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteGroupMemberGQL extends Apollo.Mutation<DeleteGroupMemberMutation, DeleteGroupMemberMutationVariables> {
    document = DeleteGroupMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ManageableLessonsDocument = gql`
    query manageableLessons {
  manageableLessons {
    ...lesson
  }
}
    ${LessonFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ManageableLessonsGQL extends Apollo.Query<ManageableLessonsQuery, ManageableLessonsQueryVariables> {
    document = ManageableLessonsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const PrepareAddLessonDocument = gql`
    query prepareAddLesson {
  manageableLessons {
    lessonId
    groupId
    slideId
  }
  authoritativeGroups(role: LESSON) {
    ...group
  }
  slides {
    ...slide
  }
}
    ${GroupFragmentDoc}
${SlideFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class PrepareAddLessonGQL extends Apollo.Query<PrepareAddLessonQuery, PrepareAddLessonQueryVariables> {
    document = PrepareAddLessonDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddLessonDocument = gql`
    mutation addLesson($command: AddLessonCommand!) {
  addLesson(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddLessonGQL extends Apollo.Mutation<AddLessonMutation, AddLessonMutationVariables> {
    document = AddLessonDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteLessonDocument = gql`
    mutation deleteLesson($lessonId: ID!) {
  deleteLesson(lessonId: $lessonId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteLessonGQL extends Apollo.Mutation<DeleteLessonMutation, DeleteLessonMutationVariables> {
    document = DeleteLessonDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const NoticesDocument = gql`
    query notices {
  notices {
    ...notice
  }
}
    ${NoticeFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class NoticesGQL extends Apollo.Query<NoticesQuery, NoticesQueryVariables> {
    document = NoticesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const EffectiveNoticesDocument = gql`
    query effectiveNotices {
  effectiveNotices {
    ...notice
  }
}
    ${NoticeFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class EffectiveNoticesGQL extends Apollo.Query<EffectiveNoticesQuery, EffectiveNoticesQueryVariables> {
    document = EffectiveNoticesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddNoticeDocument = gql`
    mutation addNotice($command: AddNoticeCommand!) {
  addNotice(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddNoticeGQL extends Apollo.Mutation<AddNoticeMutation, AddNoticeMutationVariables> {
    document = AddNoticeDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const UpdateNoticeDocument = gql`
    mutation updateNotice($command: UpdateNoticeCommand!) {
  updateNotice(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class UpdateNoticeGQL extends Apollo.Mutation<UpdateNoticeMutation, UpdateNoticeMutationVariables> {
    document = UpdateNoticeDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteNoticeDocument = gql`
    mutation deleteNotice($noticeId: ID!) {
  deleteNotice(noticeId: $noticeId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteNoticeGQL extends Apollo.Mutation<DeleteNoticeMutation, DeleteNoticeMutationVariables> {
    document = DeleteNoticeDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SlidesDocument = gql`
    query slides {
  slides {
    ...slide
  }
}
    ${SlideFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class SlidesGQL extends Apollo.Query<SlidesQuery, SlidesQueryVariables> {
    document = SlidesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddSlideDocument = gql`
    mutation addSlide($command: AddSlideCommand!) {
  addSlide(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddSlideGQL extends Apollo.Mutation<AddSlideMutation, AddSlideMutationVariables> {
    document = AddSlideDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteSlideDocument = gql`
    mutation deleteSlide($slideId: ID!) {
  deleteSlide(slideId: $slideId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteSlideGQL extends Apollo.Mutation<DeleteSlideMutation, DeleteSlideMutationVariables> {
    document = DeleteSlideDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const CurrentUserDocument = gql`
    query currentUser {
  currentUser {
    ...user
  }
}
    ${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class CurrentUserGQL extends Apollo.Query<CurrentUserQuery, CurrentUserQueryVariables> {
    document = CurrentUserDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const UsersDocument = gql`
    query users {
  userCount
  users {
    ...user
  }
  realms {
    ...realm
  }
}
    ${UserFragmentDoc}
${RealmFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class UsersGQL extends Apollo.Query<UsersQuery, UsersQueryVariables> {
    document = UsersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const RealmsDocument = gql`
    query realms {
  realms {
    ...realm
  }
}
    ${RealmFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class RealmsGQL extends Apollo.Query<RealmsQuery, RealmsQueryVariables> {
    document = RealmsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const GroupAppendableMembersDocument = gql`
    query groupAppendableMembers($groupId: ID!) {
  groupAppendableMembers(groupId: $groupId) {
    ...user
  }
}
    ${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class GroupAppendableMembersGQL extends Apollo.Query<GroupAppendableMembersQuery, GroupAppendableMembersQueryVariables> {
    document = GroupAppendableMembersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SyncRealmDocument = gql`
    mutation syncRealm($realmId: String) {
  syncRealm(realmId: $realmId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class SyncRealmGQL extends Apollo.Mutation<SyncRealmMutation, SyncRealmMutationVariables> {
    document = SyncRealmDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const MyStudiesDocument = gql`
    query myStudies {
  myStudies {
    ...study
  }
}
    ${StudyFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class MyStudiesGQL extends Apollo.Query<MyStudiesQuery, MyStudiesQueryVariables> {
    document = MyStudiesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const AddUserDocument = gql`
    mutation addUser($command: AddUserCommand!) {
  addUser(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class AddUserGQL extends Apollo.Mutation<AddUserMutation, AddUserMutationVariables> {
    document = AddUserDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const UpdateUserDocument = gql`
    mutation updateUser($command: UpdateUserCommand!) {
  updateUser(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class UpdateUserGQL extends Apollo.Mutation<UpdateUserMutation, UpdateUserMutationVariables> {
    document = UpdateUserDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ChangePasswordDocument = gql`
    mutation changePassword($command: ChangePasswordCommand!) {
  changePassword(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class ChangePasswordGQL extends Apollo.Mutation<ChangePasswordMutation, ChangePasswordMutationVariables> {
    document = ChangePasswordDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteUserDocument = gql`
    mutation deleteUser($userId: ID!) {
  deleteUser(userId: $userId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class DeleteUserGQL extends Apollo.Mutation<DeleteUserMutation, DeleteUserMutationVariables> {
    document = DeleteUserDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }