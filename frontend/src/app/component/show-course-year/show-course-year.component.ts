import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { CourseYearService } from 'src/app/service/course-year.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-course-year',
  templateUrl: './show-course-year.component.html',
  styleUrls: ['./show-course-year.component.css']
})
export class ShowCourseYearComponent extends ShowComponent<CourseYearDetails> implements OnInit {

  constructor(protected yearService: CourseYearService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) { 
      super(yearService, userService, router, route);
    }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete course year", 
      question: "Do you want to remove course year?"
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
    this.yearService.delete(id).subscribe(
      (response) => {
        this.router.navigate(['courses/'+this.elem?.parent.id+'/years']);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
