import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { Semester } from 'src/app/entity/semester';
import { SemesterService } from 'src/app/service/semester.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-program-semesters',
  templateUrl: './program-semesters.component.html',
  styleUrls: ['./program-semesters.component.css']
})
export class ProgramSemestersComponent extends PageableGetAllChildrenComponent<Semester, ProgramSummary> implements OnInit {

  constructor(protected service: SemesterService, protected userService: UserService,
    protected dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {
      super(service, userService, router, route, dialog);
      this.elemTypeName = "semester";
      this.parentTypeName = "program";
  }

  ngOnInit(): void {
      this.getFirstPage();
      this.getParent();
      this.checkAuthority("ROLE_INSTITUTE_ADMIN");
  }
}
