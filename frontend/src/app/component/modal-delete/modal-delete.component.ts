import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-delete',
  templateUrl: './modal-delete.component.html',
  styleUrls: ['./modal-delete.component.css']
})
export class ModalDeleteComponent {
  title: string;
  question: string;

  constructor(public dialogRef: MatDialogRef<ModalDeleteComponent>, 
    @Inject(MAT_DIALOG_DATA)  data: {title: string, question: string}) { 
    this.title = data.title;
    this.question = data.question;
  }

  delete(): void {
    this.dialogRef.close(true);
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
