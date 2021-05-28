import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SemesterService } from 'src/app/service/semester.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-delete-semester',
  templateUrl: './modal-delete-semester.component.html',
  styleUrls: ['./modal-delete-semester.component.css']
})
export class ModalDeleteSemesterComponent extends ModalDeleteComponent {

  constructor(public service: SemesterService, 
    public dialogRef: MatDialogRef<ModalDeleteSemesterComponent>, 
    @Inject(MAT_DIALOG_DATA) data: any) { 
      super(service, dialogRef, data);
    }

}
