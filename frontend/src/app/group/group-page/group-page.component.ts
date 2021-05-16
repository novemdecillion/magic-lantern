import { Component, Input, ViewChild, ViewEncapsulation, OnInit, OnDestroy } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { PageComponent } from 'src/app/share/page/page.component';

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

  constructor(private router: Router) {
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

}
