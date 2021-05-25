import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudyGroupService } from 'src/app/service/study-group.service';

@Component({
  selector: 'app-modal-delete-study-group',
  templateUrl: './modal-delete-study-group.component.html',
  styleUrls: ['./modal-delete-study-group.component.css']
})
export class ModalDeleteStudyGroupComponent implements OnInit {
  id: number;
  name: string;
  deletingFailed: boolean = false;
  message: string = "";

  constructor(private groupService: StudyGroupService, 
    private dialogRef: MatDialogRef<ModalDeleteStudyGroupComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      this.id = data.id;
      this.name = data.name;
    }

  ngOnInit(): void {
  }

  delete(): void {
    this.groupService.deleteStudyGroup(this.id).subscribe(
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
