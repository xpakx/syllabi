import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { StudyGroupStudentsAdapterService } from 'src/app/service/study-group-students-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalStudentDeleteComponent } from '../modal-student-delete/modal-student-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-group-students',
  templateUrl: './show-group-students.component.html',
  styleUrls: ['./show-group-students.component.css']
})
export class ShowGroupStudentsComponent extends PageableGetAllChildrenComponent<StudentWithUserId, StudyGroupSummary> implements OnInit {

  constructor(protected service: StudyGroupStudentsAdapterService, protected userService: UserService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) { 
      super(service, userService, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalStudentDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
