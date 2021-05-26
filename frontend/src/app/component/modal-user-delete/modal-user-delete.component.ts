import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';

@Component({
  selector: 'app-modal-user-delete',
  templateUrl: './modal-user-delete.component.html',
  styleUrls: ['./modal-user-delete.component.css']
})
export class ModalUserDeleteComponent extends ModalDeleteComponent {
  constructor(public userService: UserService, 
  public dialogRef: MatDialogRef<ModalUserDeleteComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(userService, dialogRef, data);
  }
}
