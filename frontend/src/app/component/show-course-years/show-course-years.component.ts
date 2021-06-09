import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { CourseYearForPage } from 'src/app/entity/course-year-for-page';
import { Page } from 'src/app/entity/page';
import { CourseYearService } from 'src/app/service/course-year.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-course-years',
  templateUrl: './show-course-years.component.html',
  styleUrls: ['./show-course-years.component.css']
})
export class ShowCourseYearsComponent extends PageableGetAllChildrenComponent<CourseYearForPage, CourseSummary> implements OnInit {
  active: boolean = true;
  parentName: string = '';
  parentId: number;

  constructor(protected service: CourseYearService, protected userService: UserService,
    protected dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route, dialog);
      this.parentId = this.id;
      this.elemTypeName = "course year";
    }

  ngOnInit(): void {
    this.ready = false;
    this.service.getAllActiveByParentId(this.parentId).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
        this.ready = true;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
        this.ready = true;
      }
    )

    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }

  getActivePage(id: number, page: number): void {
    this.ready = false;
    this.service.getAllActiveByParentIdForPage(id, page).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
        this.ready = true;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
        this.ready = true;
      }
    )
  }

  getAllPage(id: number, page: number): void {
    this.ready = false;
    this.service.getAllByParentIdForPage(id, page).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
        this.ready = true;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
        this.ready = true;
      }
    )
  }

  switchActive(): void {
    this.active = !this.active;
    this.getPage(this.page);
  }

  getPage(page: number): void {
    if(this.active) {
      this.getActivePage(this.id, page);
    }
    else {
      this.getAllPage(this.id, page);
    }
  }
}
