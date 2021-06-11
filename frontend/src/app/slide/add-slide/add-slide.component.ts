import { Component, ElementRef, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { errorMessageIfNeed } from 'src/app/utilities';
import { AddSlideGQL } from 'src/generated/graphql';

@Component({
  selector: 'app-add-slide',
  templateUrl: './add-slide.component.html',
  styles: [
  ]
})
export class AddSlideComponent {

  @ViewChild('slideFile', { static: true }) slideFileInput!: ElementRef<HTMLInputElement>;

  slideId!: string

  constructor(
    private dialogRef: MatDialogRef<AddSlideComponent>,
    private snackBar: MatSnackBar,
    private addSlideGql: AddSlideGQL) { }

  onOK() {
    let file = this.slideFileInput.nativeElement.files?.item(0);
    if (!file) {
      this.snackBar.open('教材(zip形式)ファイルを指定してください。', '確認');
      return;
    }

    this.addSlideGql.mutate({command: { slideId: this.slideId, slideFile: file }}, { context: { useMultipart: true }})
    .subscribe(res => {
      if(!errorMessageIfNeed(res, this.snackBar)) {

      // TODO 警告メッセージの表示
        this.dialogRef.close(true)
      }
    })

  }

  // uploadFileEvt() {
  //   let files = this.slideFileInput.nativeElement.files

  //   if (files && files[0]) {
  //     this.fileAttr = '';
  //     Array.from(files).forEach((file: File) => {
  //       this.fileAttr += file.name + ' - ';
  //     });

  //     // HTML5 FileReader API
  //     let reader = new FileReader();
  //     reader.onload = (e: any) => {
  //       let image = new Image();
  //       image.src = e.target.result;
  //       image.onload = rs => {
  //         let imgBase64Path = e.target.result;
  //       };
  //     };
  //     reader.readAsDataURL(files[0]);

  //   } else {
  //   }
  // }

}
