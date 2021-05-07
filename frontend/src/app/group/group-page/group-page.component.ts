import { Component, Input, ViewEncapsulation } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-group-page',
  templateUrl: './group-page.component.html',
  styleUrls: ['./group-page.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class GroupPageComponent {
  @Input() view: 'list' | 'tree' = 'list';
  @Input() dataLoad: Observable<any> | null = null;

  constructor(private router: Router) {
  }

  onChangeView(event: MatButtonToggleChange) {
    if ('tree' === event.value) {
      this.router.navigateByUrl('/group/tree');
    } else {
      this.router.navigateByUrl('/group/list');
    }
  }

}
