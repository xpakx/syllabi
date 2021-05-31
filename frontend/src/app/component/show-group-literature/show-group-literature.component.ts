import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Literature } from 'src/app/entity/literature';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { GroupLiteratureService } from 'src/app/service/group-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-group-literature',
  templateUrl: './show-group-literature.component.html',
  styleUrls: ['./show-group-literature.component.css']
})
export class ShowGroupLiteratureComponent extends ShowComponent<Literature> implements OnInit {
  group: StudyGroupSummary | undefined;

  constructor(protected literatureService: GroupLiteratureService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) {
      super(literatureService, userService, router, route);
     }

  ngOnInit(): void {
    this.getElem();


    this.literatureService.getParentById(this.id).subscribe(
      (result: StudyGroupSummary) => {
        this.group = result;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );
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
    this.literatureService.delete(id).subscribe(
      (response) => {
        //redir
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
