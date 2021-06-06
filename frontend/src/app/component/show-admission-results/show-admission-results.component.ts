import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Admission } from 'src/app/entity/admission';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { AdmissionResultsAdapterService } from 'src/app/service/admission-results-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalCloseAdmissionComponent } from '../modal-close-admission/modal-close-admission.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-admission-results',
  templateUrl: './show-admission-results.component.html',
  styleUrls: ['./show-admission-results.component.css']
})
export class ShowAdmissionResultsComponent extends PageableGetAllChildrenComponent<AdmissionForm, AdmissionDetails> implements OnInit {
  form!: FormGroup;

  constructor(protected service: AdmissionResultsAdapterService, protected userService: UserService, 
    protected dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router, private fb: FormBuilder) { 
      super(service, userService, router, route, dialog);
      this.elemTypeName = "admission form";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_ADMISSION_ADMIN");
  }

  afterGetParentSuccess(result: AdmissionDetails) {
    super.afterGetParentSuccess(result);
    this.form = this.fb.group({
      studentLimit: [result.studentLimit, Validators.pattern("^[0-9]*$")]
    });
  }  

  close() {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    
    const dialogRef = this.dialog.open(ModalCloseAdmissionComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
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
    );
  }

  changeLimit(): void {
    this.service.changeLimit(this.id, this.form.controls.studentLimit.value).subscribe(
      (response: Admission) => {
        this.getFirstPage();
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }
}
