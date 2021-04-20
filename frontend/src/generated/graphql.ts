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
};


export type MutationSyncRealmArgs = {
  realmId?: Maybe<Scalars['String']>;
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
  parentGroupTransitionId?: Maybe<Scalars['ID']>;
  memberIds?: Maybe<Array<Scalars['String']>>;
  courses?: Maybe<Array<Scalars['ID']>>;
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

export type Authority = {
  __typename?: 'Authority';
  groupId: Scalars['ID'];
  role: Scalars['String'];
};

export type User = {
  __typename?: 'User';
  userId: Scalars['ID'];
  userName: Scalars['String'];
  realmId?: Maybe<Scalars['String']>;
  enabled: Scalars['Boolean'];
  authorities: Array<Scalars['String']>;
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
  & Pick<Group, 'groupId' | 'groupOriginId' | 'groupGenerationId' | 'groupName' | 'parentGroupTransitionId' | 'memberIds' | 'courses'>
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

export type UserFragment = (
  { __typename?: 'User' }
  & Pick<User, 'userId' | 'userName' | 'realmId' | 'enabled' | 'authorities'>
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
  parentGroupTransitionId
  memberIds
  courses
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
export const UserFragmentDoc = gql`
    fragment user on User {
  userId
  userName
  realmId
  enabled
  authorities
}
    `;
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