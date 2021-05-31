import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { TeacherService } from 'src/app/service/teacher.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-teachers',
  templateUrl: './show-teachers.component.html',
  styleUrls: ['./show-teachers.component.css']
})
export class ShowTeachersComponent extends PageableGetAllComponent<TeacherSummary> implements OnInit {

  constructor(protected service: TeacherService, protected dialog: MatDialog,
  protected router: Router, protected userService: UserService) {  
    super(service, userService, router, dialog);
    this.elemTypeName = "teacher";
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.admin = true;
  }
}
