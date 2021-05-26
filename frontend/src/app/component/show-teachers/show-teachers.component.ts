import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Page } from 'src/app/entity/page';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { TeacherService } from 'src/app/service/teacher.service';
import { ModalTeacherDeleteComponent } from '../modal-teacher-delete/modal-teacher-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-teachers',
  templateUrl: './show-teachers.component.html',
  styleUrls: ['./show-teachers.component.css']
})
export class ShowTeachersComponent extends PageableGetAllComponent<TeacherSummary> implements OnInit {

  constructor(protected service: TeacherService, private dialog: MatDialog,
  protected router: Router) {  
    super(service, router);
  }

  ngOnInit(): void {
    this.getFirstPage();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalTeacherDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
