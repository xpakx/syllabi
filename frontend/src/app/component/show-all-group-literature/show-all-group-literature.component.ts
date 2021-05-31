import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { GroupLiteratureService } from 'src/app/service/group-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
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
    dialogConfig.data = {
      title: "Delete literature for group " + groupName, 
      question: "Do you want to remove literature " + name + "?"
    };
    const dialogRef = this.dialog.open(ModalDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
            this.deleteElem(id);
          }
      }
    );
  }

  deleteElem(id: number) {
    this.service.delete(id).subscribe(
      (response) => {
        this.getPage(this.page);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
