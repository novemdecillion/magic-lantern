import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { CurrentUserGQL } from 'src/generated/graphql';
import * as AppActions from '../actions/app.action';


@Injectable()
export class AppEffects {
  constructor(private actions$: Actions, private currentUserGql: CurrentUserGQL) { }

  currentUser$ = createEffect(
    () => this.actions$.pipe(
      ofType(AppActions.loadCurrentUser.type),
      switchMap(() => this.currentUserGql.fetch().pipe(
        map(res => {
          if (res?.data?.currentUser) {
            return AppActions.loadCurrentUserSuccess({ user: res.data.currentUser });
          }
          return AppActions.loadCurrentUserFail({ error: 'no data'});
        }),
        catchError(error => of(AppActions.loadCurrentUserFail({error}))))
      )
    )
  );

}
