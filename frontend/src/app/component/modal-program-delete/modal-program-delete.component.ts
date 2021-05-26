import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProgramService } from 'src/app/service/program.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-program-delete',
  templateUrl: './modal-program-delete.component.html',
  styleUrls: ['./modal-program-delete.component.css']
})
export class ModalProgramDeleteComponent extends ModalDeleteComponent {

  constructor(public programService: ProgramService, 
  public dialogRef: MatDialogRef<ModalProgramDeleteComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(programService, dialogRef, data);
  }
}
