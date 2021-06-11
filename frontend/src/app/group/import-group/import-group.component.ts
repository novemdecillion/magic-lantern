import { Component, ElementRef, Inject, TemplateRef, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { errorMessageIfNeed } from 'src/app/utilities';
import { ImportGroupGenerationGQL } from 'src/generated/graphql';
import { ConfirmDialogComponent } from 'src/app/share/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-import-group',
  templateUrl: './import-group.component.html',
  styles: [
  ]
})
export class ImportGroupComponent {
  @ViewChild('importWarning', { static: true }) private importWarningTemplate!: TemplateRef<any>;

  @ViewChild('groupFile', { static: true }) groupFileInput!: ElementRef<HTMLInputElement>;

  constructor(
    public dialog: MatDialog,
    private dialogRef: MatDialogRef<ImportGroupComponent>,
    @Inject(MAT_DIALOG_DATA) public groupGenerationId: number | null,
    private snackBar: MatSnackBar,
    private importGroupGql: ImportGroupGenerationGQL) { }

  onOK() {
    let file = this.groupFileInput.nativeElement.files?.item(0);
    if (!file) {
      this.snackBar.open('Excel(xlsx形式)ファイルを指定してください。', '確認');
      return;
    }
    if (file.type !== 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
      this.snackBar.open('Excel(xlsx形式)ファイルを指定してください。', '確認');
      return;
    }

    this.importGroupGql.mutate({command: { groupGenerationId: this.groupGenerationId, generationFile: file }}, { context: { useMultipart: true }})
        .subscribe(res => {
          if(!errorMessageIfNeed(res, this.snackBar)) {
            let warnings = res.data?.importGroupGeneration?.warnings
            if (warnings && warnings.length) {
              // 警告メッセージの表示
              this.dialog.open(ConfirmDialogComponent,
                {
                  data: {
                    title: 'グループ・インポート',
                    contentTemplateRef: this.importWarningTemplate,
                    onlyOk: true,
                    data: warnings
                  }
                });
            }
            this.dialogRef.close(true)
          }
        })

  }

}
