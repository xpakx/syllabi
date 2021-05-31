import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { CourseYearForPage } from 'src/app/entity/course-year-for-page';
import { Page } from 'src/app/entity/page';
import { CourseYearService } from 'src/app/service/course-year.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
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
    private dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route);
      this.parentId = this.id;
    }

  ngOnInit(): void {
    this.service.getAllActiveByParentId(this.parentId).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    )

    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }

  getActivePage(id: number, page: number): void {
    this.service.getAllActiveByParentIdForPage(id, page).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    )
  }

  getAllPage(id: number, page: number): void {
    this.service.getAllByParentIdForPage(id, page).subscribe(
      (response: Page<CourseYearForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
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

  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete course year", 
      question: "Do you want to remove course year?"
    };
    const dialogRef = this.dialog.open(ModalDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
            this.deleteElem(id);
          }
      }
    );
  }

  deleteElem(id: number) {
    this.service.delete(id).subscribe(
      (response) => {
        this.getPage(this.page);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
