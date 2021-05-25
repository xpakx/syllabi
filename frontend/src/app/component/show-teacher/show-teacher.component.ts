import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from 'src/app/entity/teacher';
import { TeacherService } from 'src/app/service/teacher.service';
import { ModalTeacherDeleteComponent } from '../modal-teacher-delete/modal-teacher-delete.component';

@Component({
  selector: 'app-show-teacher',
  templateUrl: './show-teacher.component.html',
  styleUrls: ['./show-teacher.component.css']
})
export class ShowTeacherComponent implements OnInit {
  teacher: Teacher | undefined;
  message: string = '';

  constructor(private teacherService: TeacherService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.teacherService.getTeacherByUserId(id).subscribe(
      (result: Teacher) => {
        this.teacher = result;
        this.teacher.user = {id: id};
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

  loadCourse(id: number): void {
    this.teacherService.getTeacherByUserId(id).subscribe(
      (result: Teacher) => {
        this.teacher = result;
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
    const dialogRef = this.dialog.open(ModalTeacherDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }

}
