import { Component, Input } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-group-page',
  templateUrl: './group-page.component.html'
})
export class GroupPageComponent {
  @Input() view: 'list' | 'tree' = 'list';
  @Input() dataLoad: Observable<any> | null = null;

  constructor(private router: Router) {
  }

  onChangeView(event: MatButtonToggleChange) {
    if ('tree' === event.value) {
      this.router.navigateByUrl('/manager/group-tree');
    } else {
      this.router.navigateByUrl('/manager/groups');
    }
  }

}
