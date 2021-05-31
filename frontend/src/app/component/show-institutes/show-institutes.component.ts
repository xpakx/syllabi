import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { InstituteForPage } from 'src/app/entity/institute-for-page';
import { Page } from 'src/app/entity/page';
import { InstituteService } from 'src/app/service/institute.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-institutes',
  templateUrl: './show-institutes.component.html',
  styleUrls: ['./show-institutes.component.css']
})
export class ShowInstitutesComponent extends PageableGetAllComponent<InstituteForPage> implements OnInit {
  
  constructor(protected service: InstituteService, protected dialog: MatDialog,
    protected router: Router, protected userService: UserService) { 
      super(service, userService, router, dialog);
      this.elemTypeName = "institute";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_INSTITUTE_ADMIN");
  }
}
