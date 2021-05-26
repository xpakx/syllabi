import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupLiteratureService } from 'src/app/service/group-literature.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-delete-group-literature',
  templateUrl: './modal-delete-group-literature.component.html',
  styleUrls: ['./modal-delete-group-literature.component.css']
})
export class ModalDeleteGroupLiteratureComponent extends ModalDeleteComponent {

  constructor(public literatureService: GroupLiteratureService, 
  public dialogRef: MatDialogRef<ModalDeleteGroupLiteratureComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(literatureService, dialogRef, data);
  }
}
