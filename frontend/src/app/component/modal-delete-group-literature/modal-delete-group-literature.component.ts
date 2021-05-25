import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LiteratureService } from 'src/app/service/literature.service';

@Component({
  selector: 'app-modal-delete-group-literature',
  templateUrl: './modal-delete-group-literature.component.html',
  styleUrls: ['./modal-delete-group-literature.component.css']
})
export class ModalDeleteGroupLiteratureComponent implements OnInit {
  id: number;
  name: string;
  courseName: string;

  constructor(private literatureService: LiteratureService, 
    private dialogRef: MatDialogRef<ModalDeleteGroupLiteratureComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
      this.courseName = data.courseName;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.literatureService.deleteGroupLiterature(this.id).subscribe(
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
