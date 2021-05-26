import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { CourseType } from 'src/app/entity/course-type';
import { Page } from 'src/app/entity/page';

@Component({
  selector: 'app-modal-course-type-choice',
  templateUrl: './modal-course-type-choice.component.html',
  styleUrls: ['./modal-course-type-choice.component.css']
})
export class ModalCourseTypeChoiceComponent implements OnInit {
  types: CourseType[] = [];
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;

  constructor(private typeService: CourseTypeService, 
    private dialogRef: MatDialogRef<ModalCourseTypeChoiceComponent>) {  }

  ngOnInit(): void {
    this.typeService.getAll().subscribe(
      (response: Page<CourseType>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getPage(page: number): void {
    this.typeService.getAllForPage(page).subscribe(
      (response: Page<CourseType>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
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

  close(num: number, name: string): void {
    this.dialogRef.close({id: num, name: name});
  }

  cancelAll() {
    this.dialogRef.close(undefined);
  }

}
