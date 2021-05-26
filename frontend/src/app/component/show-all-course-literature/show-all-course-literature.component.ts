import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Course } from 'src/app/entity/course';
import { CourseSummary } from 'src/app/entity/course-summary';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { Page } from 'src/app/entity/page';
import { CourseService } from 'src/app/service/course.service';
import { LiteratureService } from 'src/app/service/literature.service';
import { ModalDeleteCourseLiteratureComponent } from '../modal-delete-course-literature/modal-delete-course-literature.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-all-course-literature',
  templateUrl: './show-all-course-literature.component.html',
  styleUrls: ['./show-all-course-literature.component.css']
})
export class ShowAllCourseLiteratureComponent extends PageableComponent<LiteratureForPage> implements OnInit {
  course: CourseSummary | undefined;

  constructor(private literatureService: LiteratureService, private courseService: CourseService,
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) { 
      super();
     }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getAllCourseLiterature(id).subscribe(
      (response: Page<LiteratureForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );

    this.courseService.getCourseByIdMin(id).subscribe(
      (result: CourseSummary) => {
        this.course = result;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );
  }

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getAllCourseLiteratureForPage(id, page).subscribe(
      (response: Page<LiteratureForPage>) => {
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

  delete(id: number, name: string, courseName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, parentName: courseName};
    const dialogRef = this.dialog.open(ModalDeleteCourseLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }

}
