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
    protected dialog: MatDialog, protected router: Router) { 
      super(yearService, userService, router, route, dialog);
      this.elemTypeName = "course year";
    }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(['courses/'+this.elem?.parent.id+'/years']);
  }
}
