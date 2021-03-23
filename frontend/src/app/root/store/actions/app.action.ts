import { createAction, props } from '@ngrx/store';
import { UserFragment } from 'src/generated/graphql';

export const loadCurrentUser = createAction('[App] Load Current User');
export const loadCurrentUserSuccess = createAction('[App] Load Current User Success', props<{ user: UserFragment }>());
export const loadCurrentUserFail = createAction('[App] Load Current User Fail', props<{error: any}>());
