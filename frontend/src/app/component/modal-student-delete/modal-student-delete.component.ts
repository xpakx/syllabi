import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudentService } from 'src/app/service/student.service';

@Component({
  selector: 'app-modal-student-delete',
  templateUrl: './modal-student-delete.component.html',
  styleUrls: ['./modal-student-delete.component.css']
})
export class ModalStudentDeleteComponent implements OnInit {
  id: number;
  name: string;

  constructor(private studentService: StudentService, 
    private dialogRef: MatDialogRef<ModalStudentDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.studentService.deleteStudent(this.id).subscribe(
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
