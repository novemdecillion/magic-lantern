import {NgModule} from '@angular/core';
import {APOLLO_OPTIONS} from 'apollo-angular';
import {ApolloClientOptions, ApolloLink, InMemoryCache} from '@apollo/client/core';
import {HttpLink} from 'apollo-angular/http';
import { onError, ErrorResponse } from "@apollo/client/link/error";
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

const uri = '/api/v1'; // <-- add the URL of the GraphQL server here
export function createApollo(httpLink: HttpLink, matSnackBar: MatSnackBar): ApolloClientOptions<any> {
  let errorLink = onError((error: ErrorResponse) => {
    if (error.networkError?.name == 'HttpErrorResponse') {
      let httpError = error.networkError as HttpErrorResponse;
      if (httpError.status == 0) {
        matSnackBar.open('サーバと通信ができませんでした。', 'OK');
      }
    }
  });

  return {
    link: ApolloLink.from([errorLink, httpLink.create({uri})]),
    cache: new InMemoryCache(),
    defaultOptions: {
      query: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'ignore'
      },
      mutate: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'all'
      }
    }
  };
}



@NgModule({
  providers: [
    {
      provide: APOLLO_OPTIONS,
      useFactory: createApollo,
      deps: [HttpLink, MatSnackBar],
    },
  ],
})
export class GraphQLModule {}
