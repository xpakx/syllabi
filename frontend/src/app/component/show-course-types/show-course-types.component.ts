import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { Page } from 'src/app/entity/page';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { ModalDeleteCourseTypeComponent } from '../modal-delete-course-type/modal-delete-course-type.component';

@Component({
  selector: 'app-show-course-types',
  templateUrl: './show-course-types.component.html',
  styleUrls: ['./show-course-types.component.css']
})
export class ShowCourseTypesComponent implements OnInit {
  types: CourseType[] = [];
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;
  active: boolean = true;

  constructor(private typeService: CourseTypeService, private dialog: MatDialog,
    private router: Router) { }

  ngOnInit(): void {
    this.typeService.getAllCourseTypes().subscribe(
      (response: Page<CourseType>) => {
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

  printPage(response: Page<CourseType>): void {
    this.types = response.content;
    this.totalPages = response.totalPages;
    this.page = response.number;
    this.last = response.last;
    this.first = response.first;
    this.empty = response.empty;
  }

  getPage(page: number): void {
    this.typeService.getAllCourseTypesForPage(page).subscribe(
      (response: Page<CourseType>) => {
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

  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteCourseTypeComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
