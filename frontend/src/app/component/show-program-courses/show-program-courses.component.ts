import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Program } from 'src/app/entity/program';
import { ProgramCoursesAdapterService } from 'src/app/service/program-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-program-courses',
  templateUrl: './show-program-courses.component.html',
  styleUrls: ['./show-program-courses.component.css']
})
export class ShowProgramCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage, Program> implements OnInit {
  
  constructor(protected service: ProgramCoursesAdapterService, protected userService: UserService,
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
