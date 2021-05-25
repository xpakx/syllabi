import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TeacherService } from 'src/app/service/teacher.service';

@Component({
  selector: 'app-modal-teacher-delete',
  templateUrl: './modal-teacher-delete.component.html',
  styleUrls: ['./modal-teacher-delete.component.css']
})
export class ModalTeacherDeleteComponent implements OnInit {
  id: number;
  name: string;

  constructor(private teacherService: TeacherService, 
    private dialogRef: MatDialogRef<ModalTeacherDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.teacherService.deleteTeacher(this.id).subscribe(
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
