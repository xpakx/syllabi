import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Admission } from 'src/app/entity/admission';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { AdmissionResultsAdapterService } from 'src/app/service/admission-results-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-admission-results',
  templateUrl: './show-admission-results.component.html',
  styleUrls: ['./show-admission-results.component.css']
})
export class ShowAdmissionResultsComponent extends PageableGetAllChildrenComponent<AdmissionForm, AdmissionDetails> implements OnInit {

  constructor(protected service: AdmissionResultsAdapterService, protected userService: UserService, 
    protected dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route, dialog);
      this.elemTypeName = "admission form";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_ADMISSION_ADMIN");
  }

  close(): void {
    let students: number[] = this.elems.map((a) => a.id);
    this.service.close(this.id, {acceptedStudentsIds: students}).subscribe(
      (response: Admission) => {
        //
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }
}
