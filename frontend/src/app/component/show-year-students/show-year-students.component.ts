import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { Page } from 'src/app/entity/page';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { CourseYearService } from 'src/app/service/course-year.service';
import { StudentService } from 'src/app/service/student.service';
import { ModalStudentDeleteComponent } from '../modal-student-delete/modal-student-delete.component';

@Component({
  selector: 'app-show-year-students',
  templateUrl: './show-year-students.component.html',
  styleUrls: ['./show-year-students.component.css']
})
export class ShowYearStudentsComponent implements OnInit {
  students: StudentWithUserId[] = [];
  yearName: string = '';
  yearDate: string = '';
  yearId!: number;
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;

  constructor(private studentService: StudentService, private yearService: CourseYearService,
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) {  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.studentService.getAllStudentsForCourseYear(id).subscribe(
      (response: Page<StudentWithUserId>) => {
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

    this.yearService.getCourseYearById(id).subscribe(
      (result: CourseYearDetails) => {
        this.yearName = result.parent.name;  
        this.yearDate = new Date(result.startDate).getFullYear() + '/' +
        new Date(result.endDate).getFullYear();
        this.yearId = result.id;
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
    this.studentService.getAllStudentsForCourseYearForPage(id, page).subscribe(
      (response: Page<StudentWithUserId>) => {
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

  printPage(response: Page<StudentWithUserId>): void {
    this.students = response.content;
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
    const dialogRef = this.dialog.open(ModalStudentDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }

}
