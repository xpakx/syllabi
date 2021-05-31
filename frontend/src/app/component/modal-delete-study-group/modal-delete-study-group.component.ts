import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-delete-study-group',
  templateUrl: './modal-delete-study-group.component.html',
  styleUrls: ['./modal-delete-study-group.component.css']
})
export class ModalDeleteStudyGroupComponent extends ModalDeleteComponent {
  
  constructor(public groupService: StudyGroupService, 
    public dialogRef: MatDialogRef<ModalDeleteStudyGroupComponent>,
    @Inject(MAT_DIALOG_DATA) data: any) { 
      super(groupService, dialogRef, data);
    }
}
