import { Component, Inject, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { ListComponent } from 'src/app/share/list/list.component';
import { RoleMap } from 'src/app/utilities';
import { AddGroupMemberGQL, GroupAppendableMembersGQL, Role, UserFragment } from 'src/generated/graphql';

export interface MemberRecord extends UserFragment {
  selected?: boolean
}

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styles: [
  ]
})
export class AddMemberComponent implements OnInit {
  @ViewChild(ListComponent, { static: true }) private list!: ListComponent<MemberRecord>;

  dataLoad: Observable<MemberRecord[]> | null = null;

  roles: RoleMap<boolean> = {
    ADMIN: false,
    GROUP: false,
    SLIDE: false,
    LESSON: false,
    STUDY: false,
    NONE: false
  };

  constructor(
      private dialogRef: MatDialogRef<AddMemberComponent>,
      private groupAppendableMembersGql: GroupAppendableMembersGQL,
      private addGroupMemberGql: AddGroupMemberGQL,
      @Inject(MAT_DIALOG_DATA) public groupId: string) {
  }

  ngOnInit(): void {
    this.dataLoad = this.groupAppendableMembersGql
      .fetch({ groupId: this.groupId })
      .pipe(
        map(res => res.data.groupAppendableMembers),
        share()
      );
  }

  onOK() {
    let addMemberIds = this.list.dataSource.data.filter(member => member.selected).map(member => member.userId)

    let memberRoles = Object.entries(this.roles).filter(([_, value]) => value).map(([key, _]) => key as Role);
    if (0 === memberRoles.length) {
      memberRoles = [Role.None];
    }

    this.addGroupMemberGql.mutate({command: { groupId: this.groupId, userIds: addMemberIds, role: memberRoles }})
      .subscribe(_ => this.dialogRef.close(true))
  }

  get selectAll(): boolean | undefined {
    let selectedAll: boolean | undefined = undefined;
    this.list.dataSource.data.some((member, index)=> {
      let selected = member.selected ?? false
      if(0 == index) {
        selectedAll = selected
        return false
      } if (selectedAll != selected) {
        selectedAll = undefined;
        return true
      }
      return false
    })
    return selectedAll;
  }

  set selectAll(selected: boolean | undefined) {
    this.list.dataSource.data.forEach(member => member.selected = selected);
  }
}
