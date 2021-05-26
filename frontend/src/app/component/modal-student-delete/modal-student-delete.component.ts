import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudentService } from 'src/app/service/student.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-student-delete',
  templateUrl: './modal-student-delete.component.html',
  styleUrls: ['./modal-student-delete.component.css']
})
export class ModalStudentDeleteComponent extends ModalDeleteComponent {
  
  constructor(public studentService: StudentService, 
  public dialogRef: MatDialogRef<ModalStudentDeleteComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(studentService, dialogRef, data);
  }
}
