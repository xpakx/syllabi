import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { Page } from 'src/app/entity/page';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { CourseYearStudentsAdapterService } from 'src/app/service/course-year-students-adapter.service';
import { CourseYearService } from 'src/app/service/course-year.service';
import { ModalStudentDeleteComponent } from '../modal-student-delete/modal-student-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-year-students',
  templateUrl: './show-year-students.component.html',
  styleUrls: ['./show-year-students.component.css']
})
export class ShowYearStudentsComponent extends PageableGetAllChildrenComponent<StudentWithUserId> implements OnInit {
  yearName: string = '';
  yearDate: string = '';
  yearId!: number;

  constructor(protected service: CourseYearStudentsAdapterService, private yearService: CourseYearService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {  
      super(service, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.yearService.getById(this.id).subscribe(
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
