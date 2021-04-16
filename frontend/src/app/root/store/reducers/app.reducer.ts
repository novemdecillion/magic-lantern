import { createReducer, on } from '@ngrx/store';
import * as AppActions from '../actions/app.action';
import { initialState } from '../state/app.state';

export const appReducer = createReducer(
  initialState,
  on(AppActions.loadCurrentUser, state => {
    return { ...state, loading: true }
  }),
  on(AppActions.loadCurrentUserSuccess, (state, {user}) => {
    return { ...state, loading: false, user, serviceAvailable: true }
  }),
  on(AppActions.serviceUnavailableError, state => {
    return { ...state, loading: false, user: null, serviceAvailable: false }
  }),
);
