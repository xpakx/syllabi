import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseYearService } from 'src/app/service/course-year.service';

@Component({
  selector: 'app-modal-delete-course-year',
  templateUrl: './modal-delete-course-year.component.html',
  styleUrls: ['./modal-delete-course-year.component.css']
})
export class ModalDeleteCourseYearComponent implements OnInit {
  id: number;
  deletingFailed: boolean = false;
  message: string = "";

  constructor(private courseService: CourseYearService, 
    private dialogRef: MatDialogRef<ModalDeleteCourseYearComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.courseService.deleteCourseYear(this.id).subscribe(
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
