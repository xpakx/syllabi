import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { CourseYearForPage } from 'src/app/entity/course-year-for-page';
import { Page } from 'src/app/entity/page';
import { CourseYearsService } from 'src/app/service/course-years.service';
import { CourseService } from 'src/app/service/course.service';
import { ModalDeleteCourseYearComponent } from '../modal-delete-course-year/modal-delete-course-year.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-course-years',
  templateUrl: './show-course-years.component.html',
  styleUrls: ['./show-course-years.component.css']
})
export class ShowCourseYearsComponent extends PageableGetAllChildrenComponent<CourseYearForPage> implements OnInit {
  active: boolean = true;
  parentName: string = '';
  parentId: number;

  constructor(protected service: CourseYearsService, protected parentService: CourseService,
    private dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, router, route);
      this.parentId = this.id;
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.parentService.getByIdMin(this.id).subscribe(
      (response: CourseSummary) => {
        this.parentName = response.name;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  getActivePage(id: number, page: number): void {
    this.service.getAllActiveYearsForCourseForPage(id, page).subscribe(
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
    dialogConfig.data = {id: id};
    const dialogRef = this.dialog.open(ModalDeleteCourseYearComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
