import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { SemesterCoursesAdapterService } from 'src/app/service/semester-courses-adapter.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-semester-courses',
  templateUrl: './show-semester-courses.component.html',
  styleUrls: ['./show-semester-courses.component.css']
})
export class ShowSemesterCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage> implements OnInit {
  semester: SemesterSummary | undefined;

  constructor(protected service: SemesterCoursesAdapterService, private dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) {  
      super(service, router, route);
    }
  
    ngOnInit(): void {
      this.getFirstPage();
  
      this.service.getParentById(this.id).subscribe(
        (result: SemesterSummary) => {
          this.semester = result;
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
