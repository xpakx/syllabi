import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Page } from 'src/app/entity/page';
import { Program } from 'src/app/entity/program';
import { ProgramService } from 'src/app/service/program.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-program-courses',
  templateUrl: './show-program-courses.component.html',
  styleUrls: ['./show-program-courses.component.css']
})
export class ShowProgramCoursesComponent extends PageableComponent<CourseForPage> implements OnInit {
  program: Program | undefined;
  
  constructor(private programService: ProgramService, private dialog: MatDialog,
    private route: ActivatedRoute, private router: Router) {  
      super();
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programService.getAllCoursesForProgram(id).subscribe(
      (response: Page<CourseForPage>) => {
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

    this.programService.getById(id).subscribe(
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

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programService.getAllCoursesForProgramForPage(id, page).subscribe(
      (response: Page<CourseForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
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
