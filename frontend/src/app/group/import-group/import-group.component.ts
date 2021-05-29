import { Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ImportGroupGenerationGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-import-group',
  templateUrl: './import-group.component.html',
  styles: [
  ]
})
export class ImportGroupComponent {
  @ViewChild('groupFile', { static: true }) groupFileInput!: ElementRef<HTMLInputElement>;

  constructor(
    private dialogRef: MatDialogRef<ImportGroupComponent>,
    @Inject(MAT_DIALOG_DATA) public groupGenerationId: number,
    private importGroupGql: ImportGroupGenerationGQL) { }

  onOK() {
    let file = this.groupFileInput.nativeElement.files?.item(0);
    if (!file) {
      // TODO エラー
      return;
    }
    if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        // TODO エラー
      return;
    }

    this.importGroupGql.mutate({command: { groupGenerationId: this.groupGenerationId, generationFile: file }}, { context: { useMultipart: true }})
        .subscribe(_ => this.dialogRef.close(true))
  }

}
