import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { UserCoursesAdapterService } from 'src/app/service/user-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-user-courses',
  templateUrl: './show-user-courses.component.html',
  styleUrls: ['./show-user-courses.component.css']
})
export class ShowUserCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage, StudentWithUserId> implements OnInit {
  
  constructor(protected service: UserCoursesAdapterService, protected userService: UserService,
    protected dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {  
      super(service, userService, router, route, dialog);
      this.elemTypeName = "course";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
