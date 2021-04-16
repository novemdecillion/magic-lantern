import { createAction, props } from '@ngrx/store';
import { UserFragment } from 'src/generated/graphql';

export const loadCurrentUser = createAction('[App] Load Current User');
export const loadCurrentUserSuccess = createAction('[App] Load Current User Success', props<{ user: UserFragment }>());

export const serviceUnavailableError = createAction('[App] Service Unavailable', props<{error: any}>());
