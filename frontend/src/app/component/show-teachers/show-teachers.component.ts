import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Page } from 'src/app/entity/page';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { TeacherService } from 'src/app/service/teacher.service';
import { ModalTeacherDeleteComponent } from '../modal-teacher-delete/modal-teacher-delete.component';

@Component({
  selector: 'app-show-teachers',
  templateUrl: './show-teachers.component.html',
  styleUrls: ['./show-teachers.component.css']
})
export class ShowTeachersComponent implements OnInit {
  teachers: TeacherSummary[] = [];
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;

  constructor(private teacherService: TeacherService,
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) {  }

  ngOnInit(): void {
    this.teacherService.getAllTeachers().subscribe(
      (response: Page<TeacherSummary>) => {
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
  }

  getPage(page: number): void {
    this.teacherService.getAllTeachersForPage(page).subscribe(
      (response: Page<TeacherSummary>) => {
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

  printPage(response: Page<TeacherSummary>): void {
    this.teachers = response.content;
    /*for(let teacher of this.teachers) {
      teacher.user = {id: 1};
      teacher.title = "PhD"
    }*/
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
    const dialogRef = this.dialog.open(ModalTeacherDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
