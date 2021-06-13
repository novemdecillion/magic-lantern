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
  authoritativeGroupsByUser: Array<Group>;
  effectiveGroupsByUser: Array<Group>;
  isTopManageableGroupByUser: Scalars['Boolean'];
  studiesByUser: Array<INotStartStudy>;
  users: Array<User>;
  userCount: Scalars['Int'];
  realms: Array<Realm>;
  notices: Array<Notice>;
  effectiveNotices: Array<Notice>;
  slides: Array<Slide>;
  slide?: Maybe<Slide>;
  manageableLessons: Array<Lesson>;
  lesson?: Maybe<Lesson>;
  group?: Maybe<Group>;
  nextGenerationGroups: Array<Group>;
  groupMembers: Array<User>;
  groupAppendableMembers: Array<User>;
  currentGroupGenerationId: Scalars['Int'];
  currentAndNextGroupGenerations?: Maybe<Array<GroupGeneration>>;
  exportGroupGeneration?: Maybe<Scalars['String']>;
  study?: Maybe<Study>;
  lessonStudies: Array<INotStartStudy>;
};


export type QueryAuthoritativeGroupsByUserArgs = {
  role: Role;
};


export type QueryEffectiveGroupsByUserArgs = {
  role: Role;
};


export type QueryIsTopManageableGroupByUserArgs = {
  groupId: Scalars['ID'];
};


export type QuerySlideArgs = {
  slideId: Scalars['ID'];
};


export type QueryLessonArgs = {
  lessonId: Scalars['ID'];
};


export type QueryGroupArgs = {
  groupId: Scalars['ID'];
  groupGenerationId?: Maybe<Scalars['Int']>;
};


export type QueryGroupMembersArgs = {
  groupId: Scalars['ID'];
  groupGenerationId?: Maybe<Scalars['Int']>;
};


export type QueryGroupAppendableMembersArgs = {
  groupId: Scalars['ID'];
  groupGenerationId?: Maybe<Scalars['Int']>;
};


export type QueryExportGroupGenerationArgs = {
  groupGenerationId?: Maybe<Scalars['Int']>;
};


export type QueryStudyArgs = {
  studyId: Scalars['ID'];
};


export type QueryLessonStudiesArgs = {
  lessonId: Scalars['ID'];
};

export type Mutation = {
  __typename?: 'Mutation';
  syncRealm?: Maybe<Scalars['Boolean']>;
  addUser?: Maybe<Scalars['Boolean']>;
  updateUser?: Maybe<Scalars['Boolean']>;
  changePassword?: Maybe<Scalars['Boolean']>;
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
  switchGroupGeneration?: Maybe<Scalars['Boolean']>;
  importGroupGeneration?: Maybe<ImportGroupGenerationResult>;
  addSlide?: Maybe<Scalars['Boolean']>;
  deleteSlide?: Maybe<Scalars['Boolean']>;
  enableSlide?: Maybe<Scalars['Boolean']>;
  addLesson?: Maybe<Scalars['Boolean']>;
  deleteLesson?: Maybe<Scalars['Boolean']>;
  startStudy: Scalars['ID'];
  changeStudyStatus?: Maybe<Scalars['Boolean']>;
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
  command: DeleteGroupCommand;
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


export type MutationSwitchGroupGenerationArgs = {
  command: SwitchGroupGenerationCommand;
};


export type MutationImportGroupGenerationArgs = {
  command: ImportGroupGenerationCommand;
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


export type MutationStartStudyArgs = {
  slideId: Scalars['ID'];
};


export type MutationChangeStudyStatusArgs = {
  command: ChangeStudyStatus;
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
  groupGenerationId: Scalars['Int'];
  groupName: Scalars['String'];
  parentGroupId?: Maybe<Scalars['ID']>;
  path: Array<GroupCore>;
  memberCount: Scalars['Int'];
};

export type AddGroupCommand = {
  currentGenerationId: Scalars['Int'];
  targetGenerationId?: Maybe<Scalars['Int']>;
  groupName: Scalars['String'];
  parentGroupId: Scalars['ID'];
};

export type UpdateGroupCommand = {
  currentGenerationId: Scalars['Int'];
  targetGenerationId?: Maybe<Scalars['Int']>;
  groupId: Scalars['ID'];
  groupName: Scalars['String'];
  parentGroupId: Scalars['ID'];
};

export type DeleteGroupCommand = {
  currentGenerationId: Scalars['Int'];
  targetGenerationId?: Maybe<Scalars['Int']>;
  groupId: Scalars['ID'];
};

export type GroupMemberCommand = {
  currentGenerationId: Scalars['Int'];
  targetGenerationId?: Maybe<Scalars['Int']>;
  groupId: Scalars['ID'];
  userIds: Array<Scalars['ID']>;
  roles: Array<Role>;
};

export type DeleteGroupMemberCommand = {
  currentGenerationId: Scalars['Int'];
  targetGenerationId?: Maybe<Scalars['Int']>;
  groupId: Scalars['ID'];
  userIds: Array<Scalars['ID']>;
};

export type SwitchGroupGenerationCommand = {
  currentGenerationId: Scalars['Int'];
  nextGenerationId: Scalars['Int'];
};

export enum GroupApiResult {
  UnrecognizedGenerationChanged = 'UnrecognizedGenerationChanged'
}

export type ImportGroupGenerationCommand = {
  groupGenerationId?: Maybe<Scalars['Int']>;
  generationFile: Scalars['Upload'];
};

export type ImportGroupGenerationResult = {
  __typename?: 'ImportGroupGenerationResult';
  warnings?: Maybe<Array<Scalars['String']>>;
};

export type GroupGeneration = {
  __typename?: 'GroupGeneration';
  groupGenerationId: Scalars['Int'];
  isCurrent: Scalars['Boolean'];
};

export type LessonStatistics = {
  __typename?: 'LessonStatistics';
  onGoingCount: Scalars['Int'];
  passCount: Scalars['Int'];
  failCount: Scalars['Int'];
  excludedCount: Scalars['Int'];
};

export type Lesson = {
  __typename?: 'Lesson';
  lessonId: Scalars['ID'];
  groupId: Scalars['ID'];
  slideId: Scalars['ID'];
  group: Group;
  slide: Slide;
  studentCount: Scalars['Int'];
  statistics: LessonStatistics;
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

export type IChapter = {
  title: Scalars['String'];
  numberOfPages: Scalars['Int'];
};

export type ExplainChapter = IChapter & {
  __typename?: 'ExplainChapter';
  title: Scalars['String'];
  numberOfPages: Scalars['Int'];
};

export type SurveyQuestion = {
  __typename?: 'SurveyQuestion';
  type: Scalars['String'];
};

export type SurveyChapter = IChapter & {
  __typename?: 'SurveyChapter';
  title: Scalars['String'];
  numberOfPages: Scalars['Int'];
  questions: Array<SurveyQuestion>;
};

export type ExamQuestion = {
  __typename?: 'ExamQuestion';
  score?: Maybe<Scalars['Int']>;
};

export type ExamChapter = IChapter & {
  __typename?: 'ExamChapter';
  title: Scalars['String'];
  numberOfPages: Scalars['Int'];
  passScore?: Maybe<Scalars['Int']>;
  questions: Array<ExamQuestion>;
};

export type SlideConfig = {
  __typename?: 'SlideConfig';
  title: Scalars['String'];
  chapters?: Maybe<Array<IChapter>>;
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
  Study = 'STUDY'
}

export type Authority = {
  __typename?: 'Authority';
  groupId: Scalars['ID'];
  roles?: Maybe<Array<Role>>;
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
  enabled?: Maybe<Scalars['Boolean']>;
  isAdmin?: Maybe<Scalars['Boolean']>;
  isGroup?: Maybe<Scalars['Boolean']>;
  isSlide?: Maybe<Scalars['Boolean']>;
};

export type UpdateUserCommand = {
  userId: Scalars['ID'];
  accountName?: Maybe<Scalars['String']>;
  userName?: Maybe<Scalars['String']>;
  password?: Maybe<Scalars['String']>;
  email?: Maybe<Scalars['String']>;
  enabled?: Maybe<Scalars['Boolean']>;
  isAdmin?: Maybe<Scalars['Boolean']>;
  isGroup?: Maybe<Scalars['Boolean']>;
  isSlide?: Maybe<Scalars['Boolean']>;
};

export type ChangePasswordCommand = {
  oldPassword: Scalars['String'];
  newPassword: Scalars['String'];
};

export enum UserApiResult {
  UserAlreadyExist = 'UserAlreadyExist',
  UserNotFound = 'UserNotFound',
  PasswordNotMatch = 'PasswordNotMatch'
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
  Failed = 'FAILED',
  Excluded = 'EXCLUDED'
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
  answers: Array<Scalars['String']>;
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

export type INotStartStudy = {
  userId: Scalars['ID'];
  slideId: Scalars['ID'];
  status: StudyStatus;
  user: User;
  slide: Slide;
};

export type NotStartStudy = INotStartStudy & {
  __typename?: 'NotStartStudy';
  userId: Scalars['ID'];
  slideId: Scalars['ID'];
  status: StudyStatus;
  user: User;
  slide: Slide;
};

export type Study = INotStartStudy & {
  __typename?: 'Study';
  studyId: Scalars['ID'];
  userId: Scalars['ID'];
  slideId: Scalars['ID'];
  status: StudyStatus;
  progressDetails: Array<StudyProgress>;
  progressRate?: Maybe<Scalars['Int']>;
  scoreDetails: Array<StudyChapterRecord>;
  answerDetails: Array<StudyChapterAnswer>;
  startAt?: Maybe<Scalars['DateTime']>;
  endAt?: Maybe<Scalars['DateTime']>;
  user: User;
  slide: Slide;
};

export type ChangeStudyStatus = {
  userId: Scalars['ID'];
  slideId: Scalars['ID'];
  studyId?: Maybe<Scalars['ID']>;
  status: StudyStatus;
};

export type ForMemberListQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type ForMemberListQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'currentGroupGenerationId' | 'isTopManageableGroupByUser'>
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
  & Pick<Query, 'currentGroupGenerationId'>
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
  & Pick<Query, 'currentGroupGenerationId'>
  & { group?: Maybe<(
    { __typename?: 'Group' }
    & GroupFragment
  )>, groupAppendableMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type ForStudyListQueryVariables = Exact<{ [key: string]: never; }>;


export type ForStudyListQuery = (
  { __typename?: 'Query' }
  & { studiesByUser: Array<(
    { __typename?: 'NotStartStudy' }
    & NotStartStudyFragment
  ) | (
    { __typename?: 'Study' }
    & StudyFragment
  )> }
);

export type ForLessonStudentListQueryVariables = Exact<{
  lessonId: Scalars['ID'];
}>;


export type ForLessonStudentListQuery = (
  { __typename?: 'Query' }
  & { lesson?: Maybe<(
    { __typename?: 'Lesson' }
    & LessonFragment
  )>, lessonStudies: Array<(
    { __typename?: 'NotStartStudy' }
    & { user: (
      { __typename?: 'User' }
      & UserFragment
    ) }
    & NotStartStudyFragment
  ) | (
    { __typename?: 'Study' }
    & { user: (
      { __typename?: 'User' }
      & UserFragment
    ) }
    & StudyFragment
  )> }
);

export type ForLessonStudentStatusQueryVariables = Exact<{
  studyId: Scalars['ID'];
}>;


export type ForLessonStudentStatusQuery = (
  { __typename?: 'Query' }
  & { study?: Maybe<(
    { __typename?: 'Study' }
    & { user: (
      { __typename?: 'User' }
      & UserFragment
    ) }
    & StudyFragment
  )> }
);

export type GroupCoreFragment = (
  { __typename?: 'GroupCore' }
  & Pick<GroupCore, 'groupId' | 'groupName'>
);

export type GroupFragment = (
  { __typename?: 'Group' }
  & Pick<Group, 'groupId' | 'groupGenerationId' | 'groupName' | 'parentGroupId'>
  & { path: Array<(
    { __typename?: 'GroupCore' }
    & GroupCoreFragment
  )> }
);

export type GroupWithMemberCountFragment = (
  { __typename?: 'Group' }
  & Pick<Group, 'memberCount'>
  & GroupFragment
);

export type GroupGenerationFragment = (
  { __typename?: 'GroupGeneration' }
  & Pick<GroupGeneration, 'groupGenerationId' | 'isCurrent'>
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
  & { authoritativeGroupsByUser: Array<(
    { __typename?: 'Group' }
    & GroupFragment
  )> }
);

export type EffectiveGroupsQueryVariables = Exact<{
  role: Role;
}>;


export type EffectiveGroupsQuery = (
  { __typename?: 'Query' }
  & { effectiveGroupsByUser: Array<(
    { __typename?: 'Group' }
    & GroupWithMemberCountFragment
  )> }
);

export type NextGenerationGroupsQueryVariables = Exact<{ [key: string]: never; }>;


export type NextGenerationGroupsQuery = (
  { __typename?: 'Query' }
  & { currentAndNextGroupGenerations?: Maybe<Array<(
    { __typename?: 'GroupGeneration' }
    & GroupGenerationFragment
  )>>, nextGenerationGroups: Array<(
    { __typename?: 'Group' }
    & GroupFragment
  )> }
);

export type ExportGroupGenerationQueryVariables = Exact<{
  groupGenerationId?: Maybe<Scalars['Int']>;
}>;


export type ExportGroupGenerationQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'exportGroupGeneration'>
);

export type ImportGroupGenerationMutationVariables = Exact<{
  command: ImportGroupGenerationCommand;
}>;


export type ImportGroupGenerationMutation = (
  { __typename?: 'Mutation' }
  & { importGroupGeneration?: Maybe<(
    { __typename?: 'ImportGroupGenerationResult' }
    & Pick<ImportGroupGenerationResult, 'warnings'>
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
  command: DeleteGroupCommand;
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

export type CurrentAndNextGenerationQueryVariables = Exact<{ [key: string]: never; }>;


export type CurrentAndNextGenerationQuery = (
  { __typename?: 'Query' }
  & { currentAndNextGroupGenerations?: Maybe<Array<(
    { __typename?: 'GroupGeneration' }
    & GroupGenerationFragment
  )>> }
);

export type SwitchGroupGenerationMutationVariables = Exact<{
  commoand: SwitchGroupGenerationCommand;
}>;


export type SwitchGroupGenerationMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'switchGroupGeneration'>
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

export type LessonWithStatisticsFragment = (
  { __typename?: 'Lesson' }
  & Pick<Lesson, 'studentCount'>
  & { statistics: (
    { __typename?: 'LessonStatistics' }
    & Pick<LessonStatistics, 'onGoingCount' | 'passCount' | 'failCount' | 'excludedCount'>
  ) }
  & LessonFragment
);

export type ManageableLessonsQueryVariables = Exact<{ [key: string]: never; }>;


export type ManageableLessonsQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'Lesson' }
    & LessonWithStatisticsFragment
  )> }
);

export type PrepareAddLessonQueryVariables = Exact<{ [key: string]: never; }>;


export type PrepareAddLessonQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'Lesson' }
    & Pick<Lesson, 'lessonId' | 'groupId' | 'slideId'>
  )>, authoritativeGroupsByUser: Array<(
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

export type LessonStudiesQueryVariables = Exact<{
  lessonId: Scalars['ID'];
}>;


export type LessonStudiesQuery = (
  { __typename?: 'Query' }
  & { lessonStudies: Array<{ __typename?: 'NotStartStudy' } | (
    { __typename?: 'Study' }
    & StudyFragment
  )> }
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
    { __typename?: 'ExplainChapter' }
    & Pick<ExplainChapter, 'title' | 'numberOfPages'>
  ) | (
    { __typename?: 'SurveyChapter' }
    & Pick<SurveyChapter, 'title' | 'numberOfPages'>
    & { questions: Array<(
      { __typename?: 'SurveyQuestion' }
      & Pick<SurveyQuestion, 'type'>
    )> }
  ) | (
    { __typename?: 'ExamChapter' }
    & Pick<ExamChapter, 'passScore' | 'title' | 'numberOfPages'>
    & { questions: Array<(
      { __typename?: 'ExamQuestion' }
      & Pick<ExamQuestion, 'score'>
    )> }
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

export type SlideQueryVariables = Exact<{
  slideId: Scalars['ID'];
}>;


export type SlideQuery = (
  { __typename?: 'Query' }
  & { slide?: Maybe<(
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

export type NotStartStudyFragment = (
  { __typename?: 'NotStartStudy' }
  & Pick<NotStartStudy, 'userId' | 'status'>
  & { slide: (
    { __typename?: 'Slide' }
    & SlideFragment
  ) }
);

export type AnswerDetailsFragment = (
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
  )>, scoreDetails: Array<(
    { __typename?: 'StudyChapterRecord' }
    & StudyChapterRecordFragment
  )>, answerDetails: Array<(
    { __typename?: 'StudyChapterAnswer' }
    & AnswerDetailsFragment
  )>, slide: (
    { __typename?: 'Slide' }
    & SlideFragment
  ) }
);

export type StudyQueryVariables = Exact<{
  studyId: Scalars['ID'];
}>;


export type StudyQuery = (
  { __typename?: 'Query' }
  & { study?: Maybe<(
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

export type StartStadyMutationVariables = Exact<{
  slideId: Scalars['ID'];
}>;


export type StartStadyMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'startStudy'>
);

export type ChangeStudyStatusMutationVariables = Exact<{
  command: ChangeStudyStatus;
}>;


export type ChangeStudyStatusMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'changeStudyStatus'>
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
  groupGenerationId
  groupName
  parentGroupId
  path {
    ...groupCore
  }
}
    ${GroupCoreFragmentDoc}`;
export const GroupWithMemberCountFragmentDoc = gql`
    fragment groupWithMemberCount on Group {
  ...group
  memberCount
}
    ${GroupFragmentDoc}`;
export const GroupGenerationFragmentDoc = gql`
    fragment groupGeneration on GroupGeneration {
  groupGenerationId
  isCurrent
}
    `;
export const SlideConfigFragmentDoc = gql`
    fragment slideConfig on SlideConfig {
  title
  chapters {
    title
    numberOfPages
    ... on ExamChapter {
      passScore
      questions {
        score
      }
    }
    ... on SurveyChapter {
      questions {
        type
      }
    }
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
export const LessonWithStatisticsFragmentDoc = gql`
    fragment lessonWithStatistics on Lesson {
  ...lesson
  studentCount
  statistics {
    onGoingCount
    passCount
    failCount
    excludedCount
  }
}
    ${LessonFragmentDoc}`;
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
export const NotStartStudyFragmentDoc = gql`
    fragment notStartStudy on NotStartStudy {
  userId
  status
  slide {
    ...slide
  }
}
    ${SlideFragmentDoc}`;
export const StudyProgressFragmentDoc = gql`
    fragment studyProgress on StudyProgress {
  chapterIndex
  pageIndexes
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
export const AnswerDetailsFragmentDoc = gql`
    fragment answerDetails on StudyChapterAnswer {
  chapterIndex
  questions {
    questionIndex
    answers
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
  scoreDetails {
    ...studyChapterRecord
  }
  answerDetails {
    ...answerDetails
  }
  startAt
  endAt
  slide {
    ...slide
  }
}
    ${StudyProgressFragmentDoc}
${StudyChapterRecordFragmentDoc}
${AnswerDetailsFragmentDoc}
${SlideFragmentDoc}`;
export const ForMemberListDocument = gql`
    query forMemberList($groupId: ID!) {
  group(groupId: $groupId) {
    ...group
  }
  currentGroupGenerationId
  isTopManageableGroupByUser(groupId: $groupId)
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
  currentGroupGenerationId
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
  currentGroupGenerationId
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
export const ForStudyListDocument = gql`
    query forStudyList {
  studiesByUser {
    ... on Study {
      ...study
    }
    ... on NotStartStudy {
      ...notStartStudy
    }
  }
}
    ${StudyFragmentDoc}
${NotStartStudyFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForStudyListGQL extends Apollo.Query<ForStudyListQuery, ForStudyListQueryVariables> {
    document = ForStudyListDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ForLessonStudentListDocument = gql`
    query forLessonStudentList($lessonId: ID!) {
  lesson(lessonId: $lessonId) {
    ...lesson
  }
  lessonStudies(lessonId: $lessonId) {
    user {
      ...user
    }
    ... on Study {
      ...study
    }
    ... on NotStartStudy {
      ...notStartStudy
    }
  }
}
    ${LessonFragmentDoc}
${UserFragmentDoc}
${StudyFragmentDoc}
${NotStartStudyFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForLessonStudentListGQL extends Apollo.Query<ForLessonStudentListQuery, ForLessonStudentListQueryVariables> {
    document = ForLessonStudentListDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ForLessonStudentStatusDocument = gql`
    query forLessonStudentStatus($studyId: ID!) {
  study(studyId: $studyId) {
    ...study
    user {
      ...user
    }
  }
}
    ${StudyFragmentDoc}
${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ForLessonStudentStatusGQL extends Apollo.Query<ForLessonStudentStatusQuery, ForLessonStudentStatusQueryVariables> {
    document = ForLessonStudentStatusDocument;
    
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
  authoritativeGroupsByUser(role: $role) {
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
  effectiveGroupsByUser(role: $role) {
    ...groupWithMemberCount
  }
}
    ${GroupWithMemberCountFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class EffectiveGroupsGQL extends Apollo.Query<EffectiveGroupsQuery, EffectiveGroupsQueryVariables> {
    document = EffectiveGroupsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const NextGenerationGroupsDocument = gql`
    query nextGenerationGroups {
  currentAndNextGroupGenerations {
    ...groupGeneration
  }
  nextGenerationGroups {
    ...group
  }
}
    ${GroupGenerationFragmentDoc}
${GroupFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class NextGenerationGroupsGQL extends Apollo.Query<NextGenerationGroupsQuery, NextGenerationGroupsQueryVariables> {
    document = NextGenerationGroupsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ExportGroupGenerationDocument = gql`
    query exportGroupGeneration($groupGenerationId: Int) {
  exportGroupGeneration(groupGenerationId: $groupGenerationId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class ExportGroupGenerationGQL extends Apollo.Query<ExportGroupGenerationQuery, ExportGroupGenerationQueryVariables> {
    document = ExportGroupGenerationDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ImportGroupGenerationDocument = gql`
    mutation importGroupGeneration($command: ImportGroupGenerationCommand!) {
  importGroupGeneration(command: $command) {
    warnings
  }
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class ImportGroupGenerationGQL extends Apollo.Mutation<ImportGroupGenerationMutation, ImportGroupGenerationMutationVariables> {
    document = ImportGroupGenerationDocument;
    
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
    mutation deleteGroup($command: DeleteGroupCommand!) {
  deleteGroup(command: $command)
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
export const CurrentAndNextGenerationDocument = gql`
    query currentAndNextGeneration {
  currentAndNextGroupGenerations {
    ...groupGeneration
  }
}
    ${GroupGenerationFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class CurrentAndNextGenerationGQL extends Apollo.Query<CurrentAndNextGenerationQuery, CurrentAndNextGenerationQueryVariables> {
    document = CurrentAndNextGenerationDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const SwitchGroupGenerationDocument = gql`
    mutation switchGroupGeneration($commoand: SwitchGroupGenerationCommand!) {
  switchGroupGeneration(command: $commoand)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class SwitchGroupGenerationGQL extends Apollo.Mutation<SwitchGroupGenerationMutation, SwitchGroupGenerationMutationVariables> {
    document = SwitchGroupGenerationDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ManageableLessonsDocument = gql`
    query manageableLessons {
  manageableLessons {
    ...lessonWithStatistics
  }
}
    ${LessonWithStatisticsFragmentDoc}`;

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
  authoritativeGroupsByUser(role: LESSON) {
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
export const LessonStudiesDocument = gql`
    query lessonStudies($lessonId: ID!) {
  lessonStudies(lessonId: $lessonId) {
    ...study
  }
}
    ${StudyFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class LessonStudiesGQL extends Apollo.Query<LessonStudiesQuery, LessonStudiesQueryVariables> {
    document = LessonStudiesDocument;
    
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
export const SlideDocument = gql`
    query slide($slideId: ID!) {
  slide(slideId: $slideId) {
    ...slide
  }
}
    ${SlideFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class SlideGQL extends Apollo.Query<SlideQuery, SlideQueryVariables> {
    document = SlideDocument;
    
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
export const StudyDocument = gql`
    query study($studyId: ID!) {
  study(studyId: $studyId) {
    ...study
  }
}
    ${StudyFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class StudyGQL extends Apollo.Query<StudyQuery, StudyQueryVariables> {
    document = StudyDocument;
    
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
export const StartStadyDocument = gql`
    mutation startStady($slideId: ID!) {
  startStudy(slideId: $slideId)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class StartStadyGQL extends Apollo.Mutation<StartStadyMutation, StartStadyMutationVariables> {
    document = StartStadyDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const ChangeStudyStatusDocument = gql`
    mutation changeStudyStatus($command: ChangeStudyStatus!) {
  changeStudyStatus(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class ChangeStudyStatusGQL extends Apollo.Mutation<ChangeStudyStatusMutation, ChangeStudyStatusMutationVariables> {
    document = ChangeStudyStatusDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }