import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TeacherService } from 'src/app/service/teacher.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-teacher-delete',
  templateUrl: './modal-teacher-delete.component.html',
  styleUrls: ['./modal-teacher-delete.component.css']
})
export class ModalTeacherDeleteComponent extends ModalDeleteComponent {

  constructor(public teacherService: TeacherService, 
  public dialogRef: MatDialogRef<ModalTeacherDeleteComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(teacherService, dialogRef, data);
  }
}
