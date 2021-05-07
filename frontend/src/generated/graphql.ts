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
  slides: Array<Slide>;
  manageableLessons: Array<LessonWithRelation>;
  studiableLessons: Array<LessonWithRelation>;
  authoritativeGroups: Array<GroupWithPath>;
  effectiveGroups: Array<GroupWithPath>;
  group?: Maybe<GroupWithPath>;
  isTopManageableGroup: Scalars['Boolean'];
  groupMembers: Array<User>;
  groupAppendableMembers: Array<User>;
};


export type QueryAuthoritativeGroupsArgs = {
  role?: Maybe<Role>;
};


export type QueryEffectiveGroupsArgs = {
  role?: Maybe<Role>;
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
  addGroup?: Maybe<Scalars['Boolean']>;
  editGroup?: Maybe<Scalars['Boolean']>;
  deleteGroup?: Maybe<Scalars['Boolean']>;
  addGroupMember?: Maybe<Scalars['Boolean']>;
  editGroupMember?: Maybe<Scalars['Boolean']>;
  deleteGroupMember?: Maybe<Scalars['Boolean']>;
  addSlide?: Maybe<Scalars['Boolean']>;
  addLesson?: Maybe<Scalars['Boolean']>;
};


export type MutationSyncRealmArgs = {
  realmId?: Maybe<Scalars['String']>;
};


export type MutationAddGroupArgs = {
  command?: Maybe<AddGroupCommand>;
};


export type MutationEditGroupArgs = {
  command?: Maybe<EditGroupCommand>;
};


export type MutationDeleteGroupArgs = {
  groupId?: Maybe<Scalars['ID']>;
};


export type MutationAddGroupMemberArgs = {
  command?: Maybe<AddGroupMemberCommand>;
};


export type MutationEditGroupMemberArgs = {
  command?: Maybe<GroupMemberCommand>;
};


export type MutationDeleteGroupMemberArgs = {
  groupId?: Maybe<Scalars['ID']>;
  userId?: Maybe<Scalars['ID']>;
};


export type MutationAddSlideArgs = {
  command?: Maybe<AddSlideCommand>;
};


export type MutationAddLessonArgs = {
  command?: Maybe<AddLessonCommand>;
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
};

export type GroupWithPath = {
  __typename?: 'GroupWithPath';
  group: Group;
  path: Array<GroupCore>;
};

export type AddGroupCommand = {
  groupOriginId?: Maybe<Scalars['ID']>;
  groupGenerationId?: Maybe<Scalars['ID']>;
  groupName: Scalars['String'];
  parentGroupId: Scalars['ID'];
};

export type EditGroupCommand = {
  groupId: Scalars['ID'];
  groupName: Scalars['String'];
};

export type AddGroupMemberCommand = {
  groupId: Scalars['ID'];
  userId: Array<Scalars['ID']>;
  role: Array<Role>;
};

export type GroupMemberCommand = {
  groupId: Scalars['ID'];
  userId: Scalars['ID'];
  role: Array<Role>;
};

export type Lesson = {
  __typename?: 'Lesson';
  lessonId: Scalars['ID'];
  groupId: Scalars['ID'];
  slideId: Scalars['ID'];
};

export type LessonWithRelation = {
  __typename?: 'LessonWithRelation';
  lesson: Lesson;
  group: GroupWithPath;
  slide: Slide;
};

export type AddLessonCommand = {
  slideId: Scalars['ID'];
  groupIds: Array<Scalars['ID']>;
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
  userName: Scalars['String'];
  realmId?: Maybe<Scalars['String']>;
  enabled: Scalars['Boolean'];
  authorities: Array<Authority>;
};

export type Realm = {
  __typename?: 'Realm';
  realmId: Scalars['String'];
  realmName: Scalars['String'];
  enabled: Scalars['Boolean'];
  syncAt?: Maybe<Scalars['DateTime']>;
};

export type GroupMembersQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type GroupMembersQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'isTopManageableGroup'>
  & { group?: Maybe<(
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  )>, groupMembers: Array<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type GroupCoreFragment = (
  { __typename?: 'GroupCore' }
  & Pick<GroupCore, 'groupId' | 'groupName'>
);

export type GroupWithPathFragment = (
  { __typename?: 'GroupWithPath' }
  & { group: (
    { __typename?: 'Group' }
    & GroupFragment
  ), path: Array<(
    { __typename?: 'GroupCore' }
    & GroupCoreFragment
  )> }
);

export type GroupFragment = (
  { __typename?: 'Group' }
  & Pick<Group, 'groupId' | 'groupOriginId' | 'groupGenerationId' | 'groupName' | 'parentGroupId'>
);

export type GroupQueryVariables = Exact<{
  groupId: Scalars['ID'];
}>;


export type GroupQuery = (
  { __typename?: 'Query' }
  & { group?: Maybe<(
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  )> }
);

export type AuthoritativeGroupsQueryVariables = Exact<{
  role?: Maybe<Role>;
}>;


export type AuthoritativeGroupsQuery = (
  { __typename?: 'Query' }
  & { authoritativeGroups: Array<(
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  )> }
);

export type EffectiveGroupsQueryVariables = Exact<{
  role?: Maybe<Role>;
}>;


export type EffectiveGroupsQuery = (
  { __typename?: 'Query' }
  & { effectiveGroups: Array<(
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  )> }
);

export type AddGroupMutationVariables = Exact<{
  command?: Maybe<AddGroupCommand>;
}>;


export type AddGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addGroup'>
);

export type EditGroupMutationVariables = Exact<{
  command?: Maybe<EditGroupCommand>;
}>;


export type EditGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'editGroup'>
);

export type DeleteGroupMutationVariables = Exact<{
  groupId?: Maybe<Scalars['ID']>;
}>;


export type DeleteGroupMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteGroup'>
);

export type AddGroupMemberMutationVariables = Exact<{
  command?: Maybe<AddGroupMemberCommand>;
}>;


export type AddGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addGroupMember'>
);

export type EditGroupMemberMutationVariables = Exact<{
  command?: Maybe<GroupMemberCommand>;
}>;


export type EditGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'editGroupMember'>
);

export type DeleteGroupMemberMutationVariables = Exact<{
  groupId?: Maybe<Scalars['ID']>;
  userId?: Maybe<Scalars['ID']>;
}>;


export type DeleteGroupMemberMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'deleteGroupMember'>
);

export type LessonFragment = (
  { __typename?: 'Lesson' }
  & Pick<Lesson, 'lessonId' | 'groupId' | 'slideId'>
);

export type LessonWithRelationFragment = (
  { __typename?: 'LessonWithRelation' }
  & { lesson: (
    { __typename?: 'Lesson' }
    & LessonFragment
  ), group: (
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  ), slide: (
    { __typename?: 'Slide' }
    & SlideFragment
  ) }
);

export type ManageableLessonsQueryVariables = Exact<{ [key: string]: never; }>;


export type ManageableLessonsQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'LessonWithRelation' }
    & LessonWithRelationFragment
  )> }
);

export type StudiableLessonsQueryVariables = Exact<{ [key: string]: never; }>;


export type StudiableLessonsQuery = (
  { __typename?: 'Query' }
  & { studiableLessons: Array<(
    { __typename?: 'LessonWithRelation' }
    & LessonWithRelationFragment
  )> }
);

export type PrepareAddLessonQueryVariables = Exact<{ [key: string]: never; }>;


export type PrepareAddLessonQuery = (
  { __typename?: 'Query' }
  & { manageableLessons: Array<(
    { __typename?: 'LessonWithRelation' }
    & { lesson: (
      { __typename?: 'Lesson' }
      & LessonFragment
    ) }
  )>, authoritativeGroups: Array<(
    { __typename?: 'GroupWithPath' }
    & GroupWithPathFragment
  )>, slides: Array<(
    { __typename?: 'Slide' }
    & SlideFragment
  )> }
);

export type AddLessonMutationVariables = Exact<{
  command?: Maybe<AddLessonCommand>;
}>;


export type AddLessonMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addLesson'>
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
  & Pick<Slide, 'slideId'>
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
  command?: Maybe<AddSlideCommand>;
}>;


export type AddSlideMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'addSlide'>
);

export type AuthorityFragment = (
  { __typename?: 'Authority' }
  & Pick<Authority, 'groupId' | 'roles'>
);

export type UserFragment = (
  { __typename?: 'User' }
  & Pick<User, 'userId' | 'userName' | 'realmId' | 'enabled'>
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

export const LessonFragmentDoc = gql`
    fragment lesson on Lesson {
  lessonId
  groupId
  slideId
}
    `;
export const GroupFragmentDoc = gql`
    fragment group on Group {
  groupId
  groupOriginId
  groupGenerationId
  groupName
  parentGroupId
}
    `;
export const GroupCoreFragmentDoc = gql`
    fragment groupCore on GroupCore {
  groupId
  groupName
}
    `;
export const GroupWithPathFragmentDoc = gql`
    fragment groupWithPath on GroupWithPath {
  group {
    ...group
  }
  path {
    ...groupCore
  }
}
    ${GroupFragmentDoc}
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
  config {
    ...slideConfig
  }
}
    ${SlideConfigFragmentDoc}`;
export const LessonWithRelationFragmentDoc = gql`
    fragment lessonWithRelation on LessonWithRelation {
  lesson {
    ...lesson
  }
  group {
    ...groupWithPath
  }
  slide {
    ...slide
  }
}
    ${LessonFragmentDoc}
${GroupWithPathFragmentDoc}
${SlideFragmentDoc}`;
export const AuthorityFragmentDoc = gql`
    fragment authority on Authority {
  groupId
  roles
}
    `;
export const UserFragmentDoc = gql`
    fragment user on User {
  userId
  userName
  realmId
  enabled
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
export const GroupMembersDocument = gql`
    query groupMembers($groupId: ID!) {
  group(groupId: $groupId) {
    ...groupWithPath
  }
  isTopManageableGroup(groupId: $groupId)
  groupMembers(groupId: $groupId) {
    ...user
  }
}
    ${GroupWithPathFragmentDoc}
${UserFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class GroupMembersGQL extends Apollo.Query<GroupMembersQuery, GroupMembersQueryVariables> {
    document = GroupMembersDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const GroupDocument = gql`
    query group($groupId: ID!) {
  group(groupId: $groupId) {
    ...groupWithPath
  }
}
    ${GroupWithPathFragmentDoc}`;

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
    query authoritativeGroups($role: Role) {
  authoritativeGroups(role: $role) {
    ...groupWithPath
  }
}
    ${GroupWithPathFragmentDoc}`;

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
    query effectiveGroups($role: Role) {
  effectiveGroups(role: $role) {
    ...groupWithPath
  }
}
    ${GroupWithPathFragmentDoc}`;

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
    mutation addGroup($command: AddGroupCommand) {
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
export const EditGroupDocument = gql`
    mutation editGroup($command: EditGroupCommand) {
  editGroup(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class EditGroupGQL extends Apollo.Mutation<EditGroupMutation, EditGroupMutationVariables> {
    document = EditGroupDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteGroupDocument = gql`
    mutation deleteGroup($groupId: ID) {
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
    mutation addGroupMember($command: AddGroupMemberCommand) {
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
export const EditGroupMemberDocument = gql`
    mutation editGroupMember($command: GroupMemberCommand) {
  editGroupMember(command: $command)
}
    `;

  @Injectable({
    providedIn: 'root'
  })
  export class EditGroupMemberGQL extends Apollo.Mutation<EditGroupMemberMutation, EditGroupMemberMutationVariables> {
    document = EditGroupMemberDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const DeleteGroupMemberDocument = gql`
    mutation deleteGroupMember($groupId: ID, $userId: ID) {
  deleteGroupMember(groupId: $groupId, userId: $userId)
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
    ...lessonWithRelation
  }
}
    ${LessonWithRelationFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class ManageableLessonsGQL extends Apollo.Query<ManageableLessonsQuery, ManageableLessonsQueryVariables> {
    document = ManageableLessonsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const StudiableLessonsDocument = gql`
    query studiableLessons {
  studiableLessons {
    ...lessonWithRelation
  }
}
    ${LessonWithRelationFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class StudiableLessonsGQL extends Apollo.Query<StudiableLessonsQuery, StudiableLessonsQueryVariables> {
    document = StudiableLessonsDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const PrepareAddLessonDocument = gql`
    query prepareAddLesson {
  manageableLessons {
    lesson {
      ...lesson
    }
  }
  authoritativeGroups(role: LESSON) {
    ...groupWithPath
  }
  slides {
    ...slide
  }
}
    ${LessonFragmentDoc}
${GroupWithPathFragmentDoc}
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
    mutation addLesson($command: AddLessonCommand) {
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
    mutation addSlide($command: AddSlideCommand) {
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