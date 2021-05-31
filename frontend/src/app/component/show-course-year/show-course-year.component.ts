import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { CourseYearService } from 'src/app/service/course-year.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteCourseYearComponent } from '../modal-delete-course-year/modal-delete-course-year.component';
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
    dialogConfig.data = {id: id};
    const dialogRef = this.dialog.open(ModalDeleteCourseYearComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
        if(data) {
         this.router.navigate(['courses/'+this.elem?.parent.id+'/years']);
       }
      }
    );
  }

}
