import { Component, ContentChild, Directive, EventEmitter, Input, Output, TemplateRef } from '@angular/core';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';


@Directive({
  selector: '[pageTitle]',
})
export class PageTitleDirective {
  constructor(public template: TemplateRef<any>) { }
}

@Directive({
  selector: '[pageToolbar]',
})
export class PageToolbarDirective {
  constructor(public template: TemplateRef<any>) { }
}

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html'
})
export class PageComponent {
  @ContentChild(PageTitleDirective) title: PageTitleDirective | null = null;
  @ContentChild(PageToolbarDirective) toolbar: PageToolbarDirective | null = null;

  @Input() titleTemplateRef: TemplateRef<any> | null = null;
  @Input() toolbarTemplateRef: TemplateRef<any> | null = null;
  @Input()
  set dataLoad(observable: Observable<any> | null) {
    if (observable) {
      this.isLoading = true;
      observable.pipe(finalize(()=>this.isLoading = false)).subscribe();
    } else {
      this.isLoading = false;
    }
  }

  constructor() {}

  @Output() onLoadData = new EventEmitter<void>();

  isLoading: boolean = false;

  onClickLoadData() {
    this.onLoadData.emit();
  }

  isExistLoadDataListener(): boolean {
    return 0 < this.onLoadData.observers.length
  }

}
