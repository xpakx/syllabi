import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Institute } from 'src/app/entity/institute';
import { Page } from 'src/app/entity/page';
import { InstituteCoursesAdapterService } from 'src/app/service/institute-courses-adapter.service';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-institute-courses',
  templateUrl: './show-institute-courses.component.html',
  styleUrls: ['./show-institute-courses.component.css']
})
export class ShowInstituteCoursesComponent extends  PageableGetAllChildrenComponent<CourseForPage> implements OnInit {
  institute: Institute | undefined;
  
  constructor(protected service: InstituteCoursesAdapterService, private dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.service.getParentById(this.id).subscribe(
      (result: Institute) => {
        this.institute = result;
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
