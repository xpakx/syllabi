import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseTypeService } from 'src/app/service/course-type.service';

@Component({
  selector: 'app-modal-delete-course-type',
  templateUrl: './modal-delete-course-type.component.html',
  styleUrls: ['./modal-delete-course-type.component.css']
})
export class ModalDeleteCourseTypeComponent implements OnInit {
  id: number;
  name: string;

  constructor(private typeService: CourseTypeService, 
    private dialogRef: MatDialogRef<ModalDeleteCourseTypeComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.typeService.deleteCourseType(this.id).subscribe(
      (response) => {
        this.dialogRef.close();
      },
      (error: HttpErrorResponse) => {
      }
    );
  }

  cancel(): void {
    this.dialogRef.close();
  }

}
