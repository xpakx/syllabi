import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseService } from 'src/app/service/course.service';

@Component({
  selector: 'app-modal-delete-course',
  templateUrl: './modal-delete-course.component.html',
  styleUrls: ['./modal-delete-course.component.css']
})
export class ModalDeleteCourseComponent implements OnInit {
  name: string;
  id: number;
  message: string = '';
  deletingFailed: boolean = false;

  constructor(private courseService: CourseService, 
    private dialogRef: MatDialogRef<ModalDeleteCourseComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.name = data.name;
      this.id = data.id;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.courseService.deleteCourse(this.id).subscribe(
      (response) => {
        this.dialogRef.close(true);
      },
      (error: HttpErrorResponse) => {
        this.deletingFailed = true;
        this.message = error.error.message;
      }
    );
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

}
