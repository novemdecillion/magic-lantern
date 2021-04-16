import {
  ActionReducer,
  ActionReducerMap,
  createFeatureSelector,
  createSelector,
  MetaReducer
} from '@ngrx/store';
import { environment } from '../../../environments/environment';
import { AppState } from './state/app.state';
import { appReducer } from './reducers/app.reducer';

export interface State {
  app: AppState;
}

export const reducers: ActionReducerMap<State> = {
  app: appReducer
};


export const metaReducers: MetaReducer<State>[] = !environment.production ? [] : [];


export const getAppState = (state: State) => state.app;
export const getLoading = createSelector(getAppState, state => state.loading);
export const getUser = createSelector(getAppState, state => state.user);
export const getServiceAvailable = createSelector(getAppState, state => state.serviceAvailable);
