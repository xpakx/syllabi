import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { Literature } from 'src/app/entity/literature';
import { CourseLiteratureService } from 'src/app/service/course-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-course-literature',
  templateUrl: './show-course-literature.component.html',
  styleUrls: ['./show-course-literature.component.css']
})
export class ShowCourseLiteratureComponent extends ShowComponent<Literature> implements OnInit {
  course: CourseSummary | undefined;

  constructor(protected literatureService: CourseLiteratureService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {
      super(literatureService, userService, router, route, dialog);
     }

  ngOnInit(): void {
    this.getElem();


    this.literatureService.getParentById(this.id).subscribe(
      (result: CourseSummary) => {
        this.course = result;
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

  delete(id: number, name: string, courseName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete literature for course " + courseName + "?", 
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
