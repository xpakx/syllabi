import { HttpErrorResponse } from '@angular/common/http';
import { MatDialogRef } from '@angular/material/dialog';
import { ServiceWithDelete } from 'src/app/service/service-with-delete';

export abstract class ModalDeleteComponent {
  name: string;
  id: number;
  message: string = '';
  deletingFailed: boolean = false;

  constructor(public service: ServiceWithDelete, 
    public dialogRef: MatDialogRef<ModalDeleteComponent>,
    data: any) { 
      this.name = data.name;
      this.id = data.id;
    }

  delete(): void {
    this.service.delete(this.id).subscribe(
      (response) => {
        this.dialogRef.close(true);
      },
      (error: HttpErrorResponse) => {
        this.deletingFailed = true;
        this.message = error.error.message;
      }
    );
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

}
