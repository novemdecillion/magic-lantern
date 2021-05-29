import { Component, ElementRef, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';
import { createGroupNodes, GroupNode } from 'src/app/utilities';
import { ExportGroupGenerationGQL, NextGenerationGroupsGQL, SwitchGroupGenerationGQL } from 'src/generated/graphql';
import { EditMemberComponent } from '../edit-member/edit-member.component';
import { ImportGroupComponent } from '../import-group/import-group.component';
import { downloadGroupGeneration } from '../utilities';

@Component({
  selector: 'app-next-generation',
  templateUrl: './next-generation.component.html',
  styles: [
  ]
})
export class NextGenerationComponent implements OnInit {
  dataLoad: Observable<GroupNode[]> | null = null;

  currentGenerationId: number | null = null;
  nextGenerationId: number | null = null;

  constructor(
    public dialog: MatDialog,
    private groupsGql: NextGenerationGroupsGQL,
    private exportGroupGenerationGql: ExportGroupGenerationGQL,
    private switchGroupGenerationGQL: SwitchGroupGenerationGQL,
    private hostElementRef: ElementRef<HTMLElement>) { }

  ngOnInit(): void {
    this.onLoadData();
  }

  onLoadData = (): void => {
    this.dataLoad = this.groupsGql.fetch()
      .pipe(
        map(res => {
          if (2 == res.data.currentAndNextGroupGenerations?.length) {
            this.currentGenerationId = res.data.currentAndNextGroupGenerations[0].groupGenerationId;
            this.nextGenerationId = res.data.currentAndNextGroupGenerations[1].groupGenerationId;
          }
          let [nodes, _] = createGroupNodes(res.data.nextGenerationGroups)
          return Object.values(nodes);
        }),
        share()
      );
  }

  onImportGeneration() {
    this.dialog.open(ImportGroupComponent, { data: this.nextGenerationId })
      .afterClosed().subscribe(res => {
        if (res) {
          this.onLoadData();
        }
      });

  }

  onExportGeneration() {
    downloadGroupGeneration(this.exportGroupGenerationGql, this.hostElementRef.nativeElement);
  }

  onSwitchGeneration() {
    this.dialog.open(ConfirmDialogComponent, { data: { title: '世代移行', message: `次世代グループを現行グループに移行します。よろしですか?` } })
      .afterClosed().subscribe(res => {
        if (res) {
          this.switchGroupGenerationGQL.mutate({ commoand: { currentGenerationId: this.currentGenerationId!, nextGenerationId: this.nextGenerationId!} })
            .subscribe(_ => this.onLoadData());
        }
      });

  }


}
