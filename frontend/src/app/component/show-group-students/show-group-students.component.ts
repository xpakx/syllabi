import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Page } from 'src/app/entity/page';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { StudentService } from 'src/app/service/student.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalStudentDeleteComponent } from '../modal-student-delete/modal-student-delete.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-group-students',
  templateUrl: './show-group-students.component.html',
  styleUrls: ['./show-group-students.component.css']
})
export class ShowGroupStudentsComponent extends PageableComponent<StudentWithUserId> implements OnInit {
  group: StudyGroupSummary | undefined;

  constructor(private studentService: StudentService, private groupService: StudyGroupService,
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) { 
      super();
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.studentService.getAllStudentsForStudyGroup(id).subscribe(
      (response: Page<StudentWithUserId>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );

    this.groupService.getStudyGroupByIdMin(id).subscribe(
      (result: StudyGroupSummary) => {
        this.group = result;
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

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.studentService.getAllStudentsForStudyGroupForPage(id, page).subscribe(
      (response: Page<StudentWithUserId>) => {
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
    const dialogRef = this.dialog.open(ModalStudentDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
