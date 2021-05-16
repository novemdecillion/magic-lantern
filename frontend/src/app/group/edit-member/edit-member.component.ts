import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { ColumnDefinition } from 'src/app/share/list/list.component';
import { MemberRecord } from '../member-list/member-list.component';

export interface EditMemberCommand {
  members: MemberRecord[],
  type: 'new' | 'edit' | 'delete';
}

@Component({
  selector: 'app-edit-member',
  templateUrl: './edit-member.component.html',
  styles: [
  ]
})
export class EditMemberComponent implements OnInit {
  title: string;
  dataLoad: Observable<MemberRecord[]> | null = null;

  // columns: ColumnDefinition<MemberRecord>[] = [
  //   {
  //     name: 'userName',
  //     headerName: '氏名'
  //   }
  // ];

  constructor(@Inject(MAT_DIALOG_DATA) public command: EditMemberCommand) {
    this.dataLoad = of(command.members);
    switch (command.type) {
      case 'new':
        this.title = 'メンバー追加'
        break;
      case 'edit':
        this.title = '権限編集'
        break;
      case 'delete':
        this.title = 'メンバー削除'
        break;
    }
  }

  ngOnInit(): void {
  }

  onOK() {

  }
}
