import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { DeleteSlideGQL, SlideFragment, SlidesGQL } from 'src/generated/graphql';
import { AddSlideComponent } from '../add-slide/add-slide.component';

@Component({
  selector: 'app-slide-list',
  templateUrl: './slide-list.component.html'
})
export class SlideListComponent implements OnInit {

  dataLoad: Observable<SlideFragment[]> | null = null;

  constructor(public dialog: MatDialog,
      private slidesGql: SlidesGQL, private deleteSlideGql: DeleteSlideGQL) {
  }

  ngOnInit(): void {
    this.onLoadData();
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

  onDeleteSlide(row: SlideFragment) {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '教材削除', message: `教材「${row.config.title}」を削除します。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.deleteSlideGql.mutate({ slideId: row.slideId })
            .subscribe(_ => this.onLoadData());
        }
      });
  }

}
