import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Admission } from 'src/app/entity/admission';
import { AdmissionService } from 'src/app/service/admission.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-admissions',
  templateUrl: './show-admissions.component.html',
  styleUrls: ['./show-admissions.component.css']
})
export class ShowAdmissionsComponent extends PageableGetAllComponent<Admission> implements OnInit {
    
  constructor(protected service: AdmissionService, protected dialog: MatDialog,
  protected router: Router, protected userService: UserService) { 
    super(service, userService, router, dialog); 
    this.elemTypeName = "admission";
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.checkAuthority("ROLE_ADMISSION_ADMIN"); 
  }

}
