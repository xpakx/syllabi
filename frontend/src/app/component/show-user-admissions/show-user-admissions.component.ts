import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { User } from 'src/app/entity/user';
import { UserAdmissionsAdapterService } from 'src/app/service/user-admissions-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-user-admissions',
  templateUrl: './show-user-admissions.component.html',
  styleUrls: ['./show-user-admissions.component.css']
})
export class ShowUserAdmissionsComponent extends PageableGetAllChildrenComponent<AdmissionForm, User> implements OnInit {
    
  constructor(protected service: UserAdmissionsAdapterService, protected dialog: MatDialog,
  protected router: Router, protected userService: UserService, protected route: ActivatedRoute) { 
    super(service, userService, router, route, dialog); 
    this.elemTypeName = "admission form";
    if(!this.route.snapshot.paramMap.has("id")) {
      this.id = Number(localStorage.getItem("user_id"));
    }
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.admin = true;
  }
}

