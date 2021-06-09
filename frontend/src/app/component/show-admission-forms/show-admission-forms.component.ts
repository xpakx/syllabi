import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { Page } from 'src/app/entity/page';
import { AdmissionFormService } from 'src/app/service/admission-form.service';
import { AdmissionService } from 'src/app/service/admission.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-admission-forms',
  templateUrl: './show-admission-forms.component.html',
  styleUrls: ['./show-admission-forms.component.css']
})
export class ShowAdmissionFormsComponent extends PageableGetAllChildrenComponent<AdmissionForm, AdmissionDetails> implements OnInit {
  verified: boolean = false;

  constructor(protected service: AdmissionFormService, protected userService: UserService, 
    private parentService: AdmissionService, protected dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route, dialog);
      this.elemTypeName = "admission form";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_ADMISSION_ADMIN");
  }

  switchVerified(): void {
    this.verified = !this.verified;
    this.getPage(0);
  }

  getPage(page: number): void {
    if(this.verified) {
      this.getVerifiedPage(page);
    }
    else {
      super.getPage(page);
    }
  }

  getVerifiedPage(page: number): void {
    this.ready = false;
    this.service.getAllVerifiedByParentIdForPage(this.id, page).subscribe(
      (response: Page<AdmissionForm>) => {
        this.printPage(response);
        this.ready = true;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
        this.ready = true;
      }
    )
  }
}
