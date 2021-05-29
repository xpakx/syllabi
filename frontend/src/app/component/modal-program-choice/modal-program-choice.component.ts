import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Page } from 'src/app/entity/page';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { Semester } from 'src/app/entity/semester';
import { ProgramService } from 'src/app/service/program.service';
import { SemesterService } from 'src/app/service/semester.service';

@Component({
  selector: 'app-modal-program-choice',
  templateUrl: './modal-program-choice.component.html',
  styleUrls: ['./modal-program-choice.component.css']
})
export class ModalProgramChoiceComponent implements OnInit {

  programs: ProgramForPage[];
  program!: ProgramForPage;
  semesters: Semester[];
  message: string;
  totalPages: number;
  page: number;
  last: boolean;
  first: boolean;
  empty: boolean;
  choice: Semester[];
  choiceNum: number[];
  step: number = 1;

  constructor(private programService: ProgramService, private semesterService: SemesterService,
    @Inject(MAT_DIALOG_DATA) data: Semester[],
    private dialogRef: MatDialogRef<ModalProgramChoiceComponent>) { 
    this.programs = [];
    this.semesters = [];
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
    this.programService.getAll().subscribe(
      (response: Page<ProgramForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getProgramPage(page: number): void {
    this.programService.getAllForPage(page).subscribe(
      (response: Page<ProgramForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getSemesterPage(page: number): void {
    this.semesterService.getAllByParentIdForPage(this.program.id, page).subscribe(
      (response: Page<Semester>) => {
        this.printSemesterPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getPage(page: number): void {
    if(this.step===1) {
      this.getProgramPage(page);
    }
    else {
      this.getSemesterPage(page);
    }
  }

  nextStep(program: ProgramForPage): void {
    this.step = 2;
    this.program = program;
    this.getPage(0);
  }

  prevStep(): void {
    this.step = 1;
    this.getPage(0);
  }

  printPage(response: Page<ProgramForPage>): void {
    this.programs = response.content;
    this.totalPages = response.totalPages;
    this.page = response.number;
    this.last = response.last;
    this.first = response.first;
    this.empty = response.empty;
  }

  printSemesterPage(response: Page<Semester>): void {
    this.semesters = response.content;
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

  choose(semester: Semester): void {
    this.choice.push(semester);
    this.choiceNum = this.choice.map((p) => p.id);
    this.prevStep();
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
