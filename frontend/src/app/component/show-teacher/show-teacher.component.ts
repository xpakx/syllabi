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
    protected dialog: MatDialog, protected router: Router) {
        super(teacherService, userService, router, route, dialog);
        this.elemTypeName = "teacher";
     }

  ngOnInit(): void {
    this.getElem();
  }
  afterDeleteSuccess() {
    this.router.navigate(['users/'+this.elem?.user.id]);
  }
}
