import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { CourseYearStudentsAdapterService } from 'src/app/service/course-year-students-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalStudentDeleteComponent } from '../modal-student-delete/modal-student-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-year-students',
  templateUrl: './show-year-students.component.html',
  styleUrls: ['./show-year-students.component.css']
})
export class ShowYearStudentsComponent extends PageableGetAllChildrenComponent<StudentWithUserId, CourseYearDetails> implements OnInit {
  yearName: string = '';
  yearDate: string = '';
  yearId!: number;

  constructor(protected service: CourseYearStudentsAdapterService, protected userService: UserService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {  
      super(service, userService, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.service.getParentById(this.id).subscribe(
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
