import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
import { Role, SlideConfigFragment } from 'src/generated/graphql';
import { State, getUser, getServiceAvailable } from './root/store/index';
import * as AppActions from './root/store/actions/app.action'
import { tap, map } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  user$ = this.store.pipe(select(getUser));
  serviceAvailable$ = this.store.pipe(select(getServiceAvailable));

  slides: SlideConfigFragment[] = [];

  mobileQuery: MediaQueryList;

  private mobileQueryListener: () => void;

  constructor(
    private store: Store<State>,
    private changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this.mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addEventListener('change', this.mobileQueryListener);
  }

  ngOnInit(): void {
    this.store.dispatch(AppActions.loadCurrentUser());
  }

  ngOnDestroy(): void {
    this.mobileQuery.removeEventListener('change', this.mobileQueryListener);
  }

  userUniqueRoles$: Observable<Set<Role>> =
    this.user$.pipe(map(user => {
      let set = new Set<Role>();
      if (user) {
        user.authorities.reduce((accumulator, auth) => {
          auth.roles?.forEach(role => {
            accumulator.add(role);
          });
          return accumulator;
        }, set);
      }
      return set;
    }))

  hasAdminRole$
     = this.userUniqueRoles$.pipe(map(roles => roles.has(Role.Admin)))
  hasManagerRole$ =
    this.userUniqueRoles$.pipe(map(roles => roles.has(Role.Manager)))
  hasStudentRole$ =
    this.userUniqueRoles$.pipe(map(roles => roles.has(Role.Student)))
}
