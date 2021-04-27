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
};






export type Query = {
  __typename?: 'Query';
  currentUser: User;
  users: Array<User>;
  userCount: Scalars['Int'];
  realms: Array<Realm>;
  slides: Array<SlideConfig>;
  courses: Array<Course>;
  groups: Array<Group>;
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
  command?: Maybe<GroupMemberCommand>;
};


export type MutationEditGroupMemberArgs = {
  command?: Maybe<GroupMemberCommand>;
};


export type MutationDeleteGroupMemberArgs = {
  groupId?: Maybe<Scalars['ID']>;
  userId?: Maybe<Scalars['ID']>;
};

export type Member = {
  __typename?: 'Member';
  userId: Scalars['ID'];
  roles: Array<Role>;
};

export type Course = {
  __typename?: 'Course';
  courseId: Scalars['ID'];
};

export type Group = {
  __typename?: 'Group';
  groupId: Scalars['ID'];
  groupOriginId: Scalars['ID'];
  groupGenerationId: Scalars['ID'];
  groupName: Scalars['String'];
  parentGroupId?: Maybe<Scalars['ID']>;
  members?: Maybe<Array<Member>>;
  courses?: Maybe<Array<Scalars['ID']>>;
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

export type GroupMemberCommand = {
  groupId: Scalars['ID'];
  userId: Scalars['ID'];
  role: Array<Role>;
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

export enum Role {
  Admin = 'ADMIN',
  Manager = 'MANAGER',
  Student = 'STUDENT'
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

export type CourseFragment = (
  { __typename?: 'Course' }
  & Pick<Course, 'courseId'>
);

export type GroupFragment = (
  { __typename?: 'Group' }
  & Pick<Group, 'groupId' | 'groupOriginId' | 'groupGenerationId' | 'groupName' | 'parentGroupId'>
);

export type CoursesQueryVariables = Exact<{ [key: string]: never; }>;


export type CoursesQuery = (
  { __typename?: 'Query' }
  & { courses: Array<(
    { __typename?: 'Course' }
    & CourseFragment
  )> }
);

export type GroupsQueryVariables = Exact<{ [key: string]: never; }>;


export type GroupsQuery = (
  { __typename?: 'Query' }
  & { groups: Array<(
    { __typename?: 'Group' }
    & GroupFragment
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
  command?: Maybe<GroupMemberCommand>;
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

export type SlideConfigFragment = (
  { __typename?: 'SlideConfig' }
  & Pick<SlideConfig, 'title'>
  & { chapters?: Maybe<Array<(
    { __typename?: 'Chapter' }
    & Pick<Chapter, 'title'>
  )>> }
);

export type SlidesQueryVariables = Exact<{ [key: string]: never; }>;


export type SlidesQuery = (
  { __typename?: 'Query' }
  & { slides: Array<(
    { __typename?: 'SlideConfig' }
    & SlideConfigFragment
  )> }
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

export type SyncRealmMutationVariables = Exact<{
  realmId?: Maybe<Scalars['String']>;
}>;


export type SyncRealmMutation = (
  { __typename?: 'Mutation' }
  & Pick<Mutation, 'syncRealm'>
);

export const CourseFragmentDoc = gql`
    fragment course on Course {
  courseId
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
export const SlideConfigFragmentDoc = gql`
    fragment slideConfig on SlideConfig {
  title
  chapters {
    title
  }
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
export const CoursesDocument = gql`
    query courses {
  courses {
    ...course
  }
}
    ${CourseFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class CoursesGQL extends Apollo.Query<CoursesQuery, CoursesQueryVariables> {
    document = CoursesDocument;
    
    constructor(apollo: Apollo.Apollo) {
      super(apollo);
    }
  }
export const GroupsDocument = gql`
    query groups {
  groups {
    ...group
  }
}
    ${GroupFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class GroupsGQL extends Apollo.Query<GroupsQuery, GroupsQueryVariables> {
    document = GroupsDocument;
    
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
    mutation addGroupMember($command: GroupMemberCommand) {
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
export const SlidesDocument = gql`
    query slides {
  slides {
    ...slideConfig
  }
}
    ${SlideConfigFragmentDoc}`;

  @Injectable({
    providedIn: 'root'
  })
  export class SlidesGQL extends Apollo.Query<SlidesQuery, SlidesQueryVariables> {
    document = SlidesDocument;
    
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