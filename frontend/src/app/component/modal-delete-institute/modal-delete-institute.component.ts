import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InstituteService } from 'src/app/service/institute.service';

@Component({
  selector: 'app-modal-delete-institute',
  templateUrl: './modal-delete-institute.component.html',
  styleUrls: ['./modal-delete-institute.component.css']
})
export class ModalDeleteInstituteComponent implements OnInit {
  name: string;
  id: number;

  constructor(private instituteService: InstituteService, 
    private dialogRef: MatDialogRef<ModalDeleteInstituteComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.name = data.name;
      this.id = data.id;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.instituteService.deleteInstitute(this.id).subscribe(
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
