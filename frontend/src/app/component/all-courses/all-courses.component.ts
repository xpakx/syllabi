import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';
import { CourseForPage } from '../../entity/course-for-page';
import { CourseService } from '../../service/course.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-all-courses',
  templateUrl: './all-courses.component.html',
  styleUrls: ['./all-courses.component.css']
})
export class AllCoursesComponent extends PageableGetAllComponent<CourseForPage> implements OnInit {
    
  constructor(protected service: CourseService, private dialog: MatDialog,
  protected router: Router, protected userService: UserService) { 
    super(service, userService, router); 
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteCourseComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
