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

@Component({
  selector: 'app-show-user-courses',
  templateUrl: './show-user-courses.component.html',
  styleUrls: ['./show-user-courses.component.css']
})
export class ShowUserCoursesComponent implements OnInit {
  courses: CourseForPage[] = [];
  student: StudentWithUserId | undefined;
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;

  constructor(private userService: UserService, private studentService: StudentService, 
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) {  }

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

  printPage(response: Page<CourseForPage>): void {
    this.courses = response.content;
    this.totalPages = response.totalPages;
    this.page = response.number;
    this.last = response.last;
    this.first = response.first;
    this.empty = response.empty;
  }

  getPagesFull(): number[] {
    return this.getNPages(7);
  }

  getPagesMin(): number[] {
    return this.getNPages(3);
  }

  getNPages(pages: number): number[] {
    let result = [];

    let pagesToShow = Math.min(this.totalPages, pages);
  

    let leftOffset = this.page - Math.floor(pagesToShow/2);
    leftOffset = leftOffset - Math.min(0, 0+leftOffset);

    let rightOffset = Math.max(0, this.page + Math.ceil(pagesToShow/2)-this.totalPages);

    for(var i=0; i<pagesToShow; i++) {
      result.push(i+leftOffset-rightOffset);
    }

    return result;
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
