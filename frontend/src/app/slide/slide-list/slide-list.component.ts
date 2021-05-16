import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { DeleteSlideGQL, SlideFragment, SlidesGQL } from 'src/generated/graphql';
import { AddSlideComponent } from '../add-slide/add-slide.component';

@Component({
  selector: 'app-slide-list',
  templateUrl: './slide-list.component.html'
})
export class SlideListComponent implements OnInit {
  // @ViewChild('operationTemplate', { static: true }) private operationTemplate!: TemplateRef<any>;

  dataLoad: Observable<SlideFragment[]> | null = null;

  // columns: ColumnDefinition<SlideFragment>[] = [];

  constructor(public dialog: MatDialog,
      private slidesGql: SlidesGQL, private deleteSlideGql: DeleteSlideGQL) {
  }

  ngOnInit(): void {
    // this.columns = [
    //   {
    //     name: 'slideId',
    //     headerName: '教材ID'
    //   },
    //   {
    //     name: 'title',
    //     valueFrom: (_, row) => row.config.title,
    //     headerName: '教材タイトル'
    //   },
    //   {
    //     name: 'operation',
    //     headerName: null,
    //     sort: false,
    //     cellTemplate: this.operationTemplate
    //   }
    // ];

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
