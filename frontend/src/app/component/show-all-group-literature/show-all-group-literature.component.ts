import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { GroupLiteratureService } from 'src/app/service/group-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteGroupLiteratureComponent } from '../modal-delete-group-literature/modal-delete-group-literature.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-all-group-literature',
  templateUrl: './show-all-group-literature.component.html',
  styleUrls: ['./show-all-group-literature.component.css']
})
export class ShowAllGroupLiteratureComponent extends PageableGetAllChildrenComponent<LiteratureForPage, StudyGroupSummary> implements OnInit {

  constructor(protected service: GroupLiteratureService, protected userService: UserService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) { 
      super(service, userService, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }

  delete(id: number, name: string, groupName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, courseName: groupName};
    const dialogRef = this.dialog.open(ModalDeleteGroupLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }

}
