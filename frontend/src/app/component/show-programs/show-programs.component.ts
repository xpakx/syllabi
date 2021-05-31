import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { ProgramService } from 'src/app/service/program.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-programs',
  templateUrl: './show-programs.component.html',
  styleUrls: ['./show-programs.component.css']
})
export class ShowProgramsComponent extends PageableGetAllComponent<ProgramForPage> implements OnInit {
  
  constructor(protected service: ProgramService, protected dialog: MatDialog,
    protected router: Router, protected userService: UserService) { 
      super(service, userService, router, dialog);
      this.elemTypeName = "program";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
