import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-close-admission',
  templateUrl: './modal-close-admission.component.html',
  styleUrls: ['./modal-close-admission.component.css']
})
export class ModalCloseAdmissionComponent {


  constructor(public dialogRef: MatDialogRef<ModalCloseAdmissionComponent>) { }

  yes(): void {
    this.dialogRef.close(true);
  }

  no(): void {
    this.dialogRef.close(false);
  }
}
