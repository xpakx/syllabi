import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { CourseYearForPage } from 'src/app/entity/course-year-for-page';
import { Page } from 'src/app/entity/page';
import { CourseService } from 'src/app/service/course.service';
import { ModalDeleteCourseYearComponent } from '../modal-delete-course-year/modal-delete-course-year.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-course-years',
  templateUrl: './show-course-years.component.html',
  styleUrls: ['./show-course-years.component.css']
})
export class ShowCourseYearsComponent extends PageableComponent<CourseYearForPage> implements OnInit {
  active: boolean = true;
  parentId: number;
  parentName: string = '';

  constructor(private courseService: CourseService, private dialog: MatDialog, 
    private route: ActivatedRoute, private router: Router) { 
      super();
      this.parentId = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.courseService.getAllActiveYearsForCourse(id).subscribe(
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

    this.courseService.getCourseByIdMin(id).subscribe(
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
    this.courseService.getAllActiveYearsForCourseForPage(id, page).subscribe(
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
    this.courseService.getAllYearsForCourseForPage(id, page).subscribe(
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
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.active) {
      this.getActivePage(id, page);
    }
    else {
      this.getAllPage(id, page);
    }
  }

  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id,};
    const dialogRef = this.dialog.open(ModalDeleteCourseYearComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
