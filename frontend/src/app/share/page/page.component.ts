import { Component, EventEmitter, Input, Output, TemplateRef } from '@angular/core';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { PageService } from './page.service';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html'
})
export class PageComponent {
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

  constructor(public pageService: PageService) {}

  @Output() onLoadData = new EventEmitter<void>();

  isLoading: boolean = false;

  onClickLoadData() {
    this.onLoadData.emit();
    this.pageService.onLoadData();
  }

  isExistLoadDataListener(): boolean {
    return (0 < this.onLoadData.observers.length) || (0 < this.pageService.listenerCount())
  }

}
