import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Page } from 'src/app/entity/page';
import { Program } from 'src/app/entity/program';
import { ProgramCoursesAdapterService } from 'src/app/service/program-courses-adapter.service';
import { ProgramService } from 'src/app/service/program.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-program-courses',
  templateUrl: './show-program-courses.component.html',
  styleUrls: ['./show-program-courses.component.css']
})
export class ShowProgramCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage> implements OnInit {
  program: Program | undefined;
  
  constructor(protected service: ProgramCoursesAdapterService, private dialog: MatDialog,
  protected route: ActivatedRoute, protected router: Router) {  
    super(service, router, route);
  }

  ngOnInit(): void {
    this.getFirstPage();

    this.service.getParentById(this.id).subscribe(
      (result: Program) => {
        this.program = result;
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
