import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { CurrentUserGQL } from 'src/generated/graphql';
import * as AppActions from '../actions/app.action';


@Injectable()
export class AppEffects {
  constructor(private actions$: Actions, private readonly router: Router, private currentUserGql: CurrentUserGQL) { }

  readonly currentUser$ = createEffect(
    () => this.actions$.pipe(
      ofType(AppActions.loadCurrentUser.type),
      switchMap(() => this.currentUserGql.fetch().pipe(
        map(res => {
          if (res?.data?.currentUser) {
            return AppActions.loadCurrentUserSuccess({ user: res.data.currentUser });
          }
          return AppActions.serviceUnavailableError({ error: 'no data'});
        }),
        catchError(error => of(AppActions.serviceUnavailableError({error}))))
      )
    )
  );

  // readonly currentUserFail$ = createEffect(
  //   () => this.actions$.pipe(
  //     ofType(AppActions.serviceUnavailableError),
  //     tap(() => this.router.navigateByUrl('/service-unavailable'))
  //   ),
  //   { dispatch: false }
  // )

}
