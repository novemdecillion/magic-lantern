import { Injectable } from '@angular/core'
import { ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PageService {
  private eventSource = new ReplaySubject<boolean>();
  onLoadData$ = this.eventSource.asObservable();

  onLoadData() {
    this.eventSource.next(true);
  }

  listenerCount(): number {
    return this.eventSource.observers.length
  }
}
