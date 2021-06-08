import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionFormDetails } from 'src/app/entity/admission-form-details';
import { AdmissionFormService } from 'src/app/service/admission-form.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-admission-form',
  templateUrl: './show-admission-form.component.html',
  styleUrls: ['./show-admission-form.component.css']
})
export class ShowAdmissionFormComponent extends ShowComponent<AdmissionFormDetails> implements OnInit {
  constructor(protected service: AdmissionFormService, protected userService: UserService,
    protected route: ActivatedRoute, 
   protected dialog: MatDialog, protected router: Router) {  
     super(service, userService, router, route, dialog);
     this.redir = 'admissions/';
     this.elemTypeName = "admission form";
     this.parentTypeName = "form";
    }

 ngOnInit(): void {
   this.getElem();
   this.checkAuthority("ROLE_ADMISSION_ADMIN");
 }

  afterDeleteSuccess() {
    this.router.navigate(['admissions/'+this.elem?.admission.id]);
  }
 
}
