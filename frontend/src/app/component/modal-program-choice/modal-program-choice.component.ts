import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Page } from 'src/app/entity/page';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { ProgramService } from 'src/app/service/program.service';

@Component({
  selector: 'app-modal-program-choice',
  templateUrl: './modal-program-choice.component.html',
  styleUrls: ['./modal-program-choice.component.css']
})
export class ModalProgramChoiceComponent implements OnInit {

  programs: ProgramForPage[];
  message: string;
  totalPages: number;
  page: number;
  last: boolean;
  first: boolean;
  empty: boolean;
  choice: ProgramForPage[];
  choiceNum: number[];

  constructor(private programService: ProgramService, 
    @Inject(MAT_DIALOG_DATA) data: ProgramForPage[],
    private dialogRef: MatDialogRef<ModalProgramChoiceComponent>) { 
    this.programs = [];
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

  getPage(page: number): void {
    this.programService.getAllForPage(page).subscribe(
      (response: Page<ProgramForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  printPage(response: Page<ProgramForPage>): void {
    this.programs = response.content;
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

  choose(program: ProgramForPage): void {
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
