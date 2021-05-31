import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { SemesterCoursesAdapterService } from 'src/app/service/semester-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-semester-courses',
  templateUrl: './show-semester-courses.component.html',
  styleUrls: ['./show-semester-courses.component.css']
})
export class ShowSemesterCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage, SemesterSummary> implements OnInit {

  constructor(protected service: SemesterCoursesAdapterService, protected userService: UserService,
    protected dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) {  
      super(service, userService, router, route, dialog);
      this.elemTypeName = "course";
    }
  
    ngOnInit(): void {
      this.getFirstPage();
      this.getParent();
      this.checkAuthority("ROLE_COURSE_ADMIN");
    }
}
