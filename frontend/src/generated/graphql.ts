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
  LocalDateTime: any;
  LocalDate: any;
  LocalTime: any;
};






export type Query = {
  __typename?: 'Query';
  currentUser?: Maybe<User>;
  users?: Maybe<Array<User>>;
  userCount?: Maybe<Scalars['Int']>;
  realms?: Maybe<Array<Realm>>;
  slides?: Maybe<Array<SlideConfig>>;
  courses?: Maybe<Array<Course>>;
};

export type Course = {
  __typename?: 'Course';
  courseId: Scalars['ID'];
  slide: SlideConfig;
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
  enabled?: Maybe<Scalars['Boolean']>;
  authorities: Array<Scalars['String']>;
};

export type Realm = {
  __typename?: 'Realm';
  realmId: Scalars['String'];
  realmName?: Maybe<Scalars['String']>;
  syncAt?: Maybe<Scalars['LocalDateTime']>;
};

export type CourseFragment = (
  { __typename?: 'Course' }
  & Pick<Course, 'courseId'>
  & { slide: (
    { __typename?: 'SlideConfig' }
    & SlideConfigFragment
  ) }
);

export type CoursesQueryVariables = Exact<{ [key: string]: never; }>;


export type CoursesQuery = (
  { __typename?: 'Query' }
  & { courses?: Maybe<Array<(
    { __typename?: 'Course' }
    & CourseFragment
  )>> }
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
  & { slides?: Maybe<Array<(
    { __typename?: 'SlideConfig' }
    & SlideConfigFragment
  )>> }
);

export type UserFragment = (
  { __typename?: 'User' }
  & Pick<User, 'userId' | 'userName' | 'realmId' | 'enabled' | 'authorities'>
);

export type RealmFragment = (
  { __typename?: 'Realm' }
  & Pick<Realm, 'realmId' | 'realmName' | 'syncAt'>
);

export type CurrentUserQueryVariables = Exact<{ [key: string]: never; }>;


export type CurrentUserQuery = (
  { __typename?: 'Query' }
  & { currentUser?: Maybe<(
    { __typename?: 'User' }
    & UserFragment
  )> }
);

export type UsersQueryVariables = Exact<{ [key: string]: never; }>;


export type UsersQuery = (
  { __typename?: 'Query' }
  & Pick<Query, 'userCount'>
  & { users?: Maybe<Array<(
    { __typename?: 'User' }
    & UserFragment
  )>>, realms?: Maybe<Array<(
    { __typename?: 'Realm' }
    & RealmFragment
  )>> }
);

export type RealmsQueryVariables = Exact<{ [key: string]: never; }>;


export type RealmsQuery = (
  { __typename?: 'Query' }
  & { realms?: Maybe<Array<(
    { __typename?: 'Realm' }
    & RealmFragment
  )>> }
);

export const SlideConfigFragmentDoc = gql`
    fragment slideConfig on SlideConfig {
  title
  chapters {
    title
  }
}
    `;
export const CourseFragmentDoc = gql`
    fragment course on Course {
  courseId
  slide {
    ...slideConfig
  }
}
    ${SlideConfigFragmentDoc}`;
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