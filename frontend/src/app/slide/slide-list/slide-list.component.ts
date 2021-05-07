import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable, Subscription } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { PageService } from 'src/app/share/page/page.service';
import { SlideFragment, SlidesGQL } from 'src/generated/graphql';
import { AddSlideComponent } from '../add-slide/add-slide.component';

interface SlideRecord extends SlideFragment {
}

@Component({
  selector: 'app-slide-list',
  templateUrl: './slide-list.component.html'
})
export class SlideListComponent implements OnInit {

  loadDataSubscription: Subscription;
  dataLoad: Observable<SlideRecord[]> | null = null;

  columns: ColumnDefinition<SlideRecord>[] = [
    {
      name: 'slideId',
      headerName: '教材ID'
    },
    {
      name: 'title',
      valueFrom: (_, row) => row.config.title,
      headerName: '教材タイトル'
    }
  ];

  constructor(private slidesGql: SlidesGQL, public dialog: MatDialog, pageService: PageService) {
    this.loadDataSubscription = pageService.onLoadData$.subscribe(() => this.onLoadData());
  }

  ngOnInit(): void {
    this.onLoadData();
  }

  ngOnDestroy(): void {
    this.loadDataSubscription.unsubscribe();
  }

  onLoadData() {
    this.dataLoad = this.slidesGql.fetch()
      .pipe(
        map(res => {
          return res.data.slides;
        }),
        share()
      );
  }

  onAddSlide() {
    this.dialog.open(AddSlideComponent)
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });

  }

}
