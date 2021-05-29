import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { CourseLiteratureService } from 'src/app/service/course-literature.service';
import { CourseService } from 'src/app/service/course.service';
import { ModalDeleteCourseLiteratureComponent } from '../modal-delete-course-literature/modal-delete-course-literature.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-all-course-literature',
  templateUrl: './show-all-course-literature.component.html',
  styleUrls: ['./show-all-course-literature.component.css']
})
export class ShowAllCourseLiteratureComponent extends PageableGetAllChildrenComponent<LiteratureForPage, CourseSummary> implements OnInit {
  course: CourseSummary | undefined;

  constructor(protected service: CourseLiteratureService, private courseService: CourseService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) { 
      super(service, router, route);
     }

  ngOnInit(): void {
    this.getFirstPage();

    this.courseService.getByIdMin(this.id).subscribe(
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
