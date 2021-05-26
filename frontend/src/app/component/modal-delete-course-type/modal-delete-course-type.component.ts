import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-delete-course-type',
  templateUrl: './modal-delete-course-type.component.html',
  styleUrls: ['./modal-delete-course-type.component.css']
})
export class ModalDeleteCourseTypeComponent extends ModalDeleteComponent {

  constructor(public typeService: CourseTypeService, 
  public dialogRef: MatDialogRef<ModalDeleteCourseTypeComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(typeService, dialogRef, data);
  }
}
