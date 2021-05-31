import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from 'src/app/entity/teacher';
import { TeacherService } from 'src/app/service/teacher.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-teacher',
  templateUrl: './show-teacher.component.html',
  styleUrls: ['./show-teacher.component.css']
})
export class ShowTeacherComponent extends ShowComponent<Teacher> implements OnInit {
  teacher: Teacher | undefined;
  message: string = '';

  constructor(protected teacherService: TeacherService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) {
        super(teacherService, userService, router, route);
     }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete teacher", 
      question: "Do you want to remove teacher " + name + "?"
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
    this.teacherService.delete(id).subscribe(
      (response) => {
        //redir
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
