import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Institute } from 'src/app/entity/institute';
import { InstituteCoursesAdapterService } from 'src/app/service/institute-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-institute-courses',
  templateUrl: './show-institute-courses.component.html',
  styleUrls: ['./show-institute-courses.component.css']
})
export class ShowInstituteCoursesComponent extends  PageableGetAllChildrenComponent<CourseForPage, Institute> implements OnInit {
  
  constructor(protected service: InstituteCoursesAdapterService,  protected userService: UserService,
    private dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
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
