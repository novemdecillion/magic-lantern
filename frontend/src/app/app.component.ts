import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
import { Role, UserFragment } from 'src/generated/graphql';
import { State, getUser, getServiceAvailable } from './root/store/index';
import * as AppActions from './root/store/actions/app.action'
import { tap, map } from 'rxjs/operators';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { hasRootGroupAuthority, logout, roleDefine, RoleMap } from './utilities';
import { MatDialog } from '@angular/material/dialog';
import { ChangePasswordComponent } from './root/change-password/change-password.component';

interface SideMenu {
  name: string;
  icon?: string;
  link?: string;
  children?: SideMenu[];
  isEnable?: (user: UserFragment) => boolean
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
        }, {
          name: '通知',
          icon: 'mdi mdi-alert-circle',
          link: '/admin/notices'
        }
      ]
    }
  ],
  GROUP: [
    {
      name: 'グループ',
      icon: 'mdi mdi-file-tree-outline',
      link: '/group/list',
      isEnable: (user: UserFragment) => !hasRootGroupAuthority(user.authorities)
    },
    {
      name: 'グループ',
      isEnable: (user: UserFragment) => hasRootGroupAuthority(user.authorities),
      children: [
        {
          name: '現行',
          icon: 'mdi mdi-file-tree-outline',
          link: '/group/list'
        },
        {
          name: '次世代',
          icon: 'mdi mdi-page-next-outline',
          link: '/group/next',
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
      link: '/study/list'
    }
  ]
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

  isUserInSystem$: Observable<boolean> =
    this.user$.pipe(map(user => user?.isSystemRealm == true));

  serviceAvailable$ = this.store.pipe(select(getServiceAvailable));

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
      private dialog: MatDialog,
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
        let menu = SIDE_MENUS[role].filter(menu => {
          return !menu.isEnable
              || menu.isEnable(user);
        });
        this.sideMenu.data = this.sideMenu.data.concat(menu)
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

  hasChild = (_: number, node: SideMenuNode) => node.expandable;

  onChangePassword() {
    this.dialog.open(ChangePasswordComponent)
  }

  onLogout() {
    logout();
  }

}
