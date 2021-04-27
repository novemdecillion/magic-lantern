import { Component, Input, TemplateRef, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs';
import { ColumnDefinition } from '../list/list.component';
export { ColumnDefinition }

@Component({
  selector: 'app-list-page',
  templateUrl: './list-page.component.html'
})
export class ListPageComponent<Record extends {[key: string]: any}> {
  @Input() titleTemplateRef: TemplateRef<any> | null = null;
  @Input() toolbarTemplateRef: TemplateRef<any> | null = null;
  @Input() enableFilter: boolean = true;
  @Input() columns: ColumnDefinition<Record>[] = [];
  @Input() dataLoad: Observable<any> | null = null;
}
