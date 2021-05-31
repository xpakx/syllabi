import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseService } from 'src/app/service/course.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-delete-course',
  templateUrl: './modal-delete-course.component.html',
  styleUrls: ['./modal-delete-course.component.css']
})
export class ModalDeleteCourseComponent extends ModalDeleteComponent {

  constructor(public courseService: CourseService, 
  public dialogRef: MatDialogRef<ModalDeleteCourseComponent>, @Inject(MAT_DIALOG_DATA) data: any) { 
    super(courseService, dialogRef, data);
  }
}
