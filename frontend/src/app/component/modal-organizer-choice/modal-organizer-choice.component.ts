import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { InstituteForPage } from 'src/app/entity/institute-for-page';
import { Page } from 'src/app/entity/page';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { InstituteService } from 'src/app/service/institute.service';
import { MatDialogRef } from '@angular/material/dialog'

@Component({
  selector: 'app-modal-organizer-choice',
  templateUrl: './modal-organizer-choice.component.html',
  styleUrls: ['./modal-organizer-choice.component.css']
})
export class ModalOrganizerChoiceComponent implements OnInit {
  institutes: InstituteForPage[] = [];
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;

  constructor(private instituteService: InstituteService, 
    private dialogRef: MatDialogRef<ModalOrganizerChoiceComponent>) {  }

  ngOnInit(): void {
    this.instituteService.getAllInstitutes().subscribe(
      (response: Page<InstituteForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getPage(page: number): void {
    this.instituteService.getAllInstitutesForPage(page).subscribe(
      (response: Page<InstituteForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  printPage(response: Page<InstituteForPage>): void {
    this.institutes = response.content;
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
