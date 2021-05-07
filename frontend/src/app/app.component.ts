import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
import { Role, SlideConfigFragment, UserFragment } from 'src/generated/graphql';
import { State, getUser, getServiceAvailable } from './root/store/index';
import * as AppActions from './root/store/actions/app.action'
import { tap, map } from 'rxjs/operators';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { roleDefine, RoleMap } from './utilities';

interface SideMenu {
  name: string;
  icon?: string;
  link?: string;
  children?: SideMenu[];
}

const SIDE_MENUS: RoleMap<SideMenu[]> = {
  ADMIN: [
    {
      name: 'システム',
      children: [
        {
          name: 'ユーザ',
          icon: 'mdi mdi-account',
          link: '/admin/users'
        }, {
          name: '認証サーバ',
          icon: 'mdi mdi-certificate',
          link: '/admin/realms'
        },
      ]
    }
  ],
  GROUP: [
    {
      name: 'グループ',
      children: [
        {
          name: 'グループ',
          icon: 'mdi mdi-file-tree-outline',
          link: '/group/list'
        // }, {
        //   name: '世代',
//        icon: 'mdi mdi-certificate'
        }
      ]
    }
  ],
  SLIDE: [
    {
      name: '教材',
      icon: 'mdi mdi-book-open',
      link: '/slide/list'
    }
  ],
  LESSON: [
    {
      name: '講座',
      icon: 'mdi mdi-school',
      link: '/lesson/list'
    }
  ],
  STUDY: [
    {
      name: '受講',
      icon: 'mdi mdi-notebook-edit',
      link: '/study/courses'
    }
  ],
  NONE: []
};


interface SideMenuNode {
  expandable: boolean;
  name: string;
  icon?: string;
  link?: string;
  level: number;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  user$ = this.store.pipe(
    select(getUser),
    tap(user => this.createSideMenu(user)));
  serviceAvailable$ = this.store.pipe(select(getServiceAvailable));

  // slides: SlideConfigFragment[] = [];

  mobileQuery: MediaQueryList;
  private mobileQueryListener: () => void;

  private transformer = (node: SideMenu, level: number): SideMenuNode => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      icon: node.icon,
      link: node.link,
      level: level
    };
  }

  sideMenuControl = new FlatTreeControl<SideMenuNode>(
      node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
      this.transformer, node => node.level, node => node.expandable, node => node.children);

  sideMenu = new MatTreeFlatDataSource(this.sideMenuControl, this.treeFlattener);


  constructor(
      private store: Store<State>,
      changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
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

  createSideMenu(user: UserFragment | null) {
    if (null == user) {
      return;
    }

    let roles = new Set<Role>();
    user.authorities
      .reduce((accumulator, auth) => {
        auth.roles?.forEach(role => {
          accumulator.add(role);
        });
        return accumulator;
      }, roles);

    this.sideMenu.data = [];

    Array.from(roles)
    .sort((a, b) => roleDefine[a].order -  roleDefine[b].order)
    .forEach(role => {
      this.sideMenu.data = this.sideMenu.data.concat(SIDE_MENUS[role])
    })
    this.sideMenuControl.expandAll();
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

  // hasAdminRole$
  //   = this.userUniqueRoles$.pipe(map(roles => roles.has(Role.Admin)))
  // hasGroupRole$ =
  //   this.userUniqueRoles$.pipe(map(roles => roles.has(Role.
  //     Group)))
  // hasSlideRole$ =
  //   this.userUniqueRoles$.pipe(map(roles => roles.has(Role.
  //     Slide)))
  // hasCourseRole$ =
  //   this.userUniqueRoles$.pipe(map(roles => roles.has(Role.
  //     Course)))
  // hasAttendanceRole$ =
  //   this.userUniqueRoles$.pipe(map(roles => roles.has(Role.Attendance)))

  hasChild = (_: number, node: SideMenuNode) => node.expandable;
}
