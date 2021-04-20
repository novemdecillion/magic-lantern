import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { Observable, EMPTY } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html'
})
export class PageComponent<Record extends {[key: string]: any}> implements OnInit {

  @Input() titleTemplateRef: TemplateRef<any> | null = null;
  @Input() dataLoader: ()=>Observable<Record[]> = this.defaultDataLoader

  isLoading = false;

  constructor() {
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.isLoading = true
    this.dataLoader()
      .pipe(finalize(() => this.isLoading = false))
  }

  defaultDataLoader(): Observable<Record[]> {
    return EMPTY;
  }
}
