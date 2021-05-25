import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Page } from 'src/app/entity/page';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { StudentService } from 'src/app/service/student.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-user-courses',
  templateUrl: './show-user-courses.component.html',
  styleUrls: ['./show-user-courses.component.css']
})
export class ShowUserCoursesComponent extends PageableComponent<CourseForPage> implements OnInit {
  student: StudentWithUserId | undefined;
  
  constructor(private userService: UserService, private studentService: StudentService, 
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) {  
      super();
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getAllCoursesForUser(id).subscribe(
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

    this.studentService.getStudentByUserId(id).subscribe(
      (result: StudentWithUserId) => {
        this.student = result;
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
    this.userService.getAllCoursesForUserForPage(id, page).subscribe(
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
