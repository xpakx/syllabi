import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { Semester } from 'src/app/entity/semester';
import { SemesterService } from 'src/app/service/semester.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteSemesterComponent } from '../modal-delete-semester/modal-delete-semester.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-program-semesters',
  templateUrl: './program-semesters.component.html',
  styleUrls: ['./program-semesters.component.css']
})
export class ProgramSemestersComponent extends PageableGetAllChildrenComponent<Semester, ProgramSummary> implements OnInit {

  constructor(protected service: SemesterService, protected userService: UserService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {
      super(service, userService, router, route);
  }

  ngOnInit(): void {
      this.getFirstPage();
      this.getParent();
  }

  delete(id: number, name: string, programName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, parentName: programName};
    const dialogRef = this.dialog.open(ModalDeleteSemesterComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
