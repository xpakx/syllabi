import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { Literature } from 'src/app/entity/literature';
import { CourseLiteratureService } from 'src/app/service/course-literature.service';
import { CourseService } from 'src/app/service/course.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteCourseLiteratureComponent } from '../modal-delete-course-literature/modal-delete-course-literature.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-course-literature',
  templateUrl: './show-course-literature.component.html',
  styleUrls: ['./show-course-literature.component.css']
})
export class ShowCourseLiteratureComponent extends ShowComponent<Literature> implements OnInit {
  literature: Literature | undefined;
  message: string = '';
  course: CourseSummary | undefined;

  constructor(protected literatureService: CourseLiteratureService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) {
      super(literatureService, userService, router, route);
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
    dialogConfig.data = {id: id, name: name, parentName: courseName};
    const dialogRef = this.dialog.open(ModalDeleteCourseLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }


}
