import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Admission } from 'src/app/entity/admission';
import { Program } from 'src/app/entity/program';
import { ProgramAdmissionsAdapterService } from 'src/app/service/program-admissions-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-program-admissions',
  templateUrl: './show-program-admissions.component.html',
  styleUrls: ['./show-program-admissions.component.css']
})
export class ShowProgramAdmissionsComponent extends PageableGetAllChildrenComponent<Admission, Program> implements OnInit {
    
  constructor(protected service: ProgramAdmissionsAdapterService, protected dialog: MatDialog,
  protected router: Router, protected userService: UserService, protected route: ActivatedRoute) { 
    super(service, userService, router, route, dialog); 
    this.elemTypeName = "admission";
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_ADMISSION_ADMIN"); 
  }
}
