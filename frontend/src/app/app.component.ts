import { MediaMatcher } from '@angular/cdk/layout';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { SlideConfigFragment, SlidesGQL } from 'src/generated/graphql';
import { State, getUser } from './root/store';
import * as AppActions from './root/store/actions/app.action'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  user$ = this.store.pipe(select(getUser));

  slides: SlideConfigFragment[] = [];

  mobileQuery: MediaQueryList;

  private mobileQueryListener: () => void;

  constructor(
    private store: Store<State>,
    private slideGql: SlidesGQL, changeDetectorRef: ChangeDetectorRef, media: MediaMatcher) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this.mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addEventListener('change', this.mobileQueryListener);
  }

  ngOnInit(): void {
    this.store.dispatch(AppActions.loadCurrentUser());

    // setInterval(() => {
    //   this.fetchSlides();
    // }, 60_000 * 2);
  }

  // fetchSlides(): void {
  //   this.slideGql.fetch()
  //     .subscribe(res => {
  //       this.slides = res.data.slides ?? [];
  //     });
  // }
}
