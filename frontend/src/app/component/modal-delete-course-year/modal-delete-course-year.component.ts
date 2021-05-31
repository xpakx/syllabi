import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseYearService } from 'src/app/service/course-year.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-delete-course-year',
  templateUrl: './modal-delete-course-year.component.html',
  styleUrls: ['./modal-delete-course-year.component.css']
})
export class ModalDeleteCourseYearComponent extends ModalDeleteComponent {

  constructor(public yearService: CourseYearService, 
  public dialogRef: MatDialogRef<ModalDeleteCourseYearComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(yearService, dialogRef, data);
  }

}
