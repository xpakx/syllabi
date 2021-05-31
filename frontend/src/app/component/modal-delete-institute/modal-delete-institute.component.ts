import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-delete-institute',
  templateUrl: './modal-delete-institute.component.html',
  styleUrls: ['./modal-delete-institute.component.css']
})
export class ModalDeleteInstituteComponent extends ModalDeleteComponent {

  constructor(public instituteService: InstituteService, 
  public dialogRef: MatDialogRef<ModalDeleteInstituteComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(instituteService, dialogRef, data);
  }
}
