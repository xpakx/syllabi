import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-modal-user-delete',
  templateUrl: './modal-user-delete.component.html',
  styleUrls: ['./modal-user-delete.component.css']
})
export class ModalUserDeleteComponent implements OnInit {
  id: number;
  name: string;

  constructor(private userService: UserService, 
    private dialogRef: MatDialogRef<ModalUserDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.userService.deleteUser(this.id).subscribe(
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
