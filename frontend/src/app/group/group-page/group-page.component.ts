import { Component, Input, ViewChild, ViewEncapsulation, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { Router } from '@angular/router';
import { select, Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { getUser, State } from 'src/app/root/store';
import { PageComponent } from 'src/app/share/page/page.component';
import { hasRootGroupAuthority } from 'src/app/utilities';
import { ExportGroupGenerationGQL, UserFragment } from 'src/generated/graphql';
import { downloadGroupGeneration } from '../utilities';

@Component({
  selector: 'app-group-page',
  templateUrl: './group-page.component.html',
  styleUrls: ['./group-page.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GroupPageComponent implements OnInit, OnDestroy {
  @ViewChild(PageComponent, {static: true}) page!: PageComponent;

  @Input() view: 'list' | 'tree' = 'list';
  @Input() dataLoad: Observable<any> | null = null;
  @Input() onLoadData: (() => void) | null = null;

  subscription: Subscription | null = null;

  user$ = this.store.pipe(select(getUser));

  constructor(private router: Router,
              private store: Store<State>,
              private exportGroupGenerationGql: ExportGroupGenerationGQL,
              private hostElementRef: ElementRef<HTMLElement>) {
  }

  ngOnInit(): void {
    if (this.onLoadData) {
      this.subscription = this.page.onLoadData.subscribe(()=>this.onLoadData!!())
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onChangeView(event: MatButtonToggleChange) {
    if ('tree' === event.value) {
      this.router.navigateByUrl('/group/tree');
    } else {
      this.router.navigateByUrl('/group/list');
    }
  }

  onExportGeneration() {
    downloadGroupGeneration(this.exportGroupGenerationGql, this.hostElementRef.nativeElement);
  }

  hasRootGroupAuthority(user: UserFragment | null): boolean {
    if (user) {
      return hasRootGroupAuthority(user.authorities);
    }
    return false;
  }
}
