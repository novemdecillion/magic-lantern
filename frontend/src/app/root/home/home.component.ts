import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EffectiveNoticesGQL, NoticeFragment } from 'src/generated/graphql';
import { map, share } from 'rxjs/operators';
import parseISO from 'date-fns/parseISO';
import { format } from 'date-fns';
import { sortNotices } from 'src/app/utilities';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  dataLoad: Observable<NoticeFragment[]> | null = null;

  constructor(private noticesGql: EffectiveNoticesGQL) { }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData() {
    this.dataLoad = this.noticesGql.fetch()
      .pipe(
        map(res => {
          let notices = res.data.effectiveNotices.map(notice => {
            if (notice.endAt) {
              notice.message += format(parseISO(notice.endAt), ' (掲載終了:yyyy/MM/dd)')
            }
            return notice;
          });
          return sortNotices(notices);
        }),
        share()
      );
  }

}
