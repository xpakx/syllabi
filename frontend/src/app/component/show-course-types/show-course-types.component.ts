import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-course-types',
  templateUrl: './show-course-types.component.html',
  styleUrls: ['./show-course-types.component.css']
})
export class ShowCourseTypesComponent extends PageableGetAllComponent<CourseType> implements OnInit {
  
  constructor(protected service: CourseTypeService, protected dialog: MatDialog,
    protected router: Router, protected userService: UserService) {
      super(service, userService, router, dialog);
      this.elemTypeName = "course type";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
