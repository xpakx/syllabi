import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Institute } from 'src/app/entity/institute';
import { InstituteCoursesAdapterService } from 'src/app/service/institute-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-institute-courses',
  templateUrl: './show-institute-courses.component.html',
  styleUrls: ['./show-institute-courses.component.css']
})
export class ShowInstituteCoursesComponent extends  PageableGetAllChildrenComponent<CourseForPage, Institute> implements OnInit {
  
  constructor(protected service: InstituteCoursesAdapterService,  protected userService: UserService,
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
