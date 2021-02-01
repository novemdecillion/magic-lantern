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
};



export type Query = {
  __typename?: 'Query';
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