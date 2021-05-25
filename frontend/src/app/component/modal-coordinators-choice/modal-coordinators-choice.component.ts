import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Page } from 'src/app/entity/page';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { TeacherService } from 'src/app/service/teacher.service';

@Component({
  selector: 'app-modal-coordinators-choice',
  templateUrl: './modal-coordinators-choice.component.html',
  styleUrls: ['./modal-coordinators-choice.component.css']
})
export class ModalCoordinatorsChoiceComponent implements OnInit {

  coordinators: TeacherSummary[];
  message: string;
  totalPages: number;
  page: number;
  last: boolean;
  first: boolean;
  empty: boolean;
  choice: TeacherSummary[];
  choiceNum: number[];

  constructor(private teacherService: TeacherService, 
    @Inject(MAT_DIALOG_DATA) data: TeacherSummary[],
    private dialogRef: MatDialogRef<ModalCoordinatorsChoiceComponent>) { 
    this.coordinators = [];
    this.message = '';
    this.totalPages = 0;
    this.page = 0;
    this.last = true;
    this.first = true;
    this.empty = true;
    this.choice = data;
    this.choiceNum = data.map((p) => p.id);
  }

  ngOnInit(): void {
    this.teacherService.getAllTeachers().subscribe(
      (response: Page<TeacherSummary>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getPage(page: number): void {
    this.teacherService.getAllTeachersForPage(page).subscribe(
      (response: Page<TeacherSummary>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  printPage(response: Page<TeacherSummary>): void {
    this.coordinators = response.content;
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

  choose(program: TeacherSummary): void {
    this.choice.push(program);
    this.choiceNum = this.choice.map((p) => p.id);
  }

  cancel(id: number): void {
    for( var i = 0; i < this.choice.length; i++){ 
    
      if ( this.choice[i].id === id) { 
  
          this.choice.splice(i, 1); 
      }
    }
    this.choiceNum = this.choice.map((p) => p.id);
  }

  close(): void {
    this.dialogRef.close(this.choice);
  }

  cancelAll() {
    this.dialogRef.close();
  }
}
