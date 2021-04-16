import { createSelector } from '@ngrx/store';
import { UserFragment } from 'src/generated/graphql';

export interface AppState {
  serviceAvailable: boolean;
  loading: boolean;
  user: UserFragment | null;
}

export const initialState: AppState = {
  serviceAvailable: true,
  loading: false,
  user: null
};
