import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionService } from 'src/app/service/admission.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-admission',
  templateUrl: './show-admission.component.html',
  styleUrls: ['./show-admission.component.css']
})
export class ShowAdmissionComponent extends ShowComponent<AdmissionDetails> implements OnInit {

  constructor(protected service: AdmissionService, protected userService: UserService,
     protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {  
      super(service, userService, router, route, dialog);
      this.redir = 'admissions/';
      this.deleteRedir = 'admissions/';
      this.elemTypeName = "admission";
     }

  ngOnInit(): void {
    this.getElem();
  }
}
