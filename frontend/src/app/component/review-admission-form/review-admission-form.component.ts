import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { AdmissionFormDetails } from 'src/app/entity/admission-form-details';
import { AdmissionPoints } from 'src/app/entity/admission-points';
import { AdmissionPointsReview } from 'src/app/entity/admission-points-review';
import { AdmissionFormService } from 'src/app/service/admission-form.service';

@Component({
  selector: 'app-review-admission-form',
  templateUrl: './review-admission-form.component.html',
  styleUrls: ['./review-admission-form.component.css']
})
export class ReviewAdmissionFormComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  admissionForm!: AdmissionFormDetails;

  constructor(private formService: AdmissionFormService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) {   }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.formService.getById(id).subscribe(
      (result: AdmissionFormDetails) => {
        this.admissionForm = result;
        this.form = this.fb.group({
          name: [this.admissionForm.name, Validators.required],
          surname: [this.admissionForm.surname, Validators.required],
          documentId: [this.admissionForm.documentId, Validators.required],
          verify: [''],
        });
        for(let points of this.admissionForm.points) {
          this.form.addControl(""+points.id, new FormControl(points.points, Validators.pattern("^[0-9]*$")));
        }
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );

        
   }

  review(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    let points: AdmissionPointsReview[] = []
    for(let field in this.form.controls) {
      let id = Number(field);
      if(!isNaN(id)) {
        points.push({pointsId: id, points: Number(this.form.controls[field].value)});
      }
    }

    if(this.form.valid) {
      this.formService.review(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value,
        documentId: this.form.controls.documentId.value,
        verify: this.form.controls.verify.value
      }).subscribe(
        (response: AdmissionForm) => {
          
        },
        (error: HttpErrorResponse) => {
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          this.message = error.error.message;
          this.loginInvalid = true;
        }
      )
    }
  }
}
