import { HttpErrorResponse } from '@angular/common/http';
import { isNgContainer } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionForm } from 'src/app/entity/admission-form';
import { AdmissionPoints } from 'src/app/entity/admission-points';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { AdmissionFormService } from 'src/app/service/admission-form.service';
import { StudentService } from 'src/app/service/student.service';

@Component({
  selector: 'app-apply',
  templateUrl: './apply.component.html',
  styleUrls: ['./apply.component.css']
})
export class ApplyComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  student: StudentWithUserId | undefined
  admission!: AdmissionDetails;

  constructor(private studentService: StudentService, private formService: AdmissionFormService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) {   }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    const userId = Number(localStorage.getItem("user_id"));

    combineLatest(
      [this.studentService.getById(userId).pipe(
        catchError(err => of(null))
      ),
      this.formService.getParentById(id)]
    ).pipe(
      map(([student, admission]) => {
        this.admission = admission;
        this.form = this.fb.group({
          name: [student ? student.name : '', Validators.required],
          surname: [student ? student.surname : '', Validators.required],
          documentId: ["", Validators.required],
        });
        for(let weight of admission.weights) {
          this.form.addControl(""+weight.id, new FormControl("", Validators.pattern("^[0-9]*$")));
        }
      })      
    ).subscribe();
   }

  apply(): void {
    let points: AdmissionPoints[] = []
    for(let field in this.form.controls) {
      let id = Number(field);
      if(!isNaN(id)) {
        points.push({weightId: id, points: Number(this.form.controls[field].value)});
      }
    }

    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.formService.addNew(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value,
        documentId: this.form.controls.documentId.value,
        points: points
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
