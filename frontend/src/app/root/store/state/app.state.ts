import { createSelector } from '@ngrx/store';
import { UserFragment } from 'src/generated/graphql';

export interface AppState {
  loading: boolean;
  user: UserFragment | null;
}

export const initialState: AppState = {
  loading: false,
  user: null
};
