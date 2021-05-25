import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProgramService } from 'src/app/service/program.service';

@Component({
  selector: 'app-modal-program-delete',
  templateUrl: './modal-program-delete.component.html',
  styleUrls: ['./modal-program-delete.component.css']
})
export class ModalProgramDeleteComponent implements OnInit {
  id: number;
  name: string;

  constructor(private programService: ProgramService, 
    private dialogRef: MatDialogRef<ModalProgramDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.programService.deleteProgram(this.id).subscribe(
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
