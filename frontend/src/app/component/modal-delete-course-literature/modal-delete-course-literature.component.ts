import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CourseLiteratureService } from 'src/app/service/course-literature.service';
import { ModalDeleteComponent } from '../modal-delete-abstract/modal-delete.component';

@Component({
  selector: 'app-modal-delete-course-literature',
  templateUrl: './modal-delete-course-literature.component.html',
  styleUrls: ['./modal-delete-course-literature.component.css']
})
export class ModalDeleteCourseLiteratureComponent extends ModalDeleteComponent {

  constructor(public literatureService: CourseLiteratureService, 
  public dialogRef: MatDialogRef<ModalDeleteCourseLiteratureComponent>,
  @Inject(MAT_DIALOG_DATA) data: any) { 
    super(literatureService, dialogRef, data);
  }
}
