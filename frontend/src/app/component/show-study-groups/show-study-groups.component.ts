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
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-study-groups',
  templateUrl: './show-study-groups.component.html',
  styleUrls: ['./show-study-groups.component.css']
})
export class ShowStudyGroupsComponent extends PageableComponent<StudyGroupForPage> implements OnInit {
  parentId: number;
  parentName: string = '';
  parentDate: string = '';

  constructor(private yearService: CourseYearService, private dialog: MatDialog, 
    private route: ActivatedRoute, private router: Router) { 
      super();
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
