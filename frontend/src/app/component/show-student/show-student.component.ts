import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { StudentService } from 'src/app/service/student.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-student',
  templateUrl: './show-student.component.html',
  styleUrls: ['./show-student.component.css']
})
export class ShowStudentComponent extends ShowComponent<StudentWithUserId> implements OnInit {
  student: StudentWithUserId | undefined;
  message: string = '';

  constructor(protected studentService: StudentService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {
      super(studentService, userService, router, route, dialog);
     }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete student", 
      question: "Do you want to remove student " + name + "?"
    };
    const dialogRef = this.dialog.open(ModalDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
            this.deleteElem(id);
          }
      }
    );
  }

  deleteElem(id: number) {
    this.studentService.delete(id).subscribe(
      (response) => {
        //redir
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
