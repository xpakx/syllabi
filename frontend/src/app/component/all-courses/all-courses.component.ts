import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { CourseForPage } from '../../entity/course-for-page';
import { CourseService } from '../../service/course.service';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-all-courses',
  templateUrl: './all-courses.component.html',
  styleUrls: ['./all-courses.component.css']
})
export class AllCoursesComponent extends PageableGetAllComponent<CourseForPage> implements OnInit {
    
  constructor(protected service: CourseService, protected dialog: MatDialog,
  protected router: Router, protected userService: UserService) { 
    super(service, userService, router, dialog); 
    this.elemTypeName = "course";
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
