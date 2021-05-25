import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { CourseYearForPage } from 'src/app/entity/course-year-for-page';
import { Page } from 'src/app/entity/page';
import { StudyGroupForPage } from 'src/app/entity/study-group-for-page';
import { CourseYearService } from 'src/app/service/course-year.service';
import { ModalDeleteStudyGroupComponent } from '../modal-delete-study-group/modal-delete-study-group.component';

@Component({
  selector: 'app-show-study-groups',
  templateUrl: './show-study-groups.component.html',
  styleUrls: ['./show-study-groups.component.css']
})
export class ShowStudyGroupsComponent implements OnInit {
  groups: StudyGroupForPage[] = [];
  message: string = '';
  totalPages: number = 0;
  page: number = 0;
  last: boolean = true;
  first: boolean = true;
  empty: boolean = true;
  active: boolean = true;
  parentId: number;
  parentName: string = '';
  parentDate: string = '';

  constructor(private yearService: CourseYearService, private dialog: MatDialog, 
    private route: ActivatedRoute, private router: Router) { 
      this.parentId = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.yearService.getAllGroupsForYear(id).subscribe(
      (response: Page<StudyGroupForPage>) => {
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

    this.yearService.getCourseYearById(id).subscribe(
      (response: CourseYearDetails) => {
        this.parentName = response.parent.name;  
        this.parentDate = new Date(response.startDate).getFullYear() + '/' +
        new Date(response.endDate).getFullYear();
        this.parentId = response.id;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  printPage(response: Page<StudyGroupForPage>): void {
    this.groups = response.content;
    this.totalPages = response.totalPages;
    this.page = response.number;
    this.last = response.last;
    this.first = response.first;
    this.empty = response.empty;
  }

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.yearService.getAllGroupsForYearForPage(id, page).subscribe(
      (response: Page<StudyGroupForPage>) => {
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

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteStudyGroupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
