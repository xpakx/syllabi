import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LiteratureService } from 'src/app/service/literature.service';

@Component({
  selector: 'app-modal-delete-course-literature',
  templateUrl: './modal-delete-course-literature.component.html',
  styleUrls: ['./modal-delete-course-literature.component.css']
})
export class ModalDeleteCourseLiteratureComponent implements OnInit {
  id: number;
  name: string;
  courseName: string;

  constructor(private literatureService: LiteratureService, 
    private dialogRef: MatDialogRef<ModalDeleteCourseLiteratureComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
      this.courseName = data.courseName;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.literatureService.deleteCourseLiterature(this.id).subscribe(
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
