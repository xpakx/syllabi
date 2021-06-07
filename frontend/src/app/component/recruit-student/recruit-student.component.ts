import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionFormDetails } from 'src/app/entity/admission-form-details';
import { StudentProgram } from 'src/app/entity/student-program';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { AdmissionFormService } from 'src/app/service/admission-form.service';
import { StudentService } from 'src/app/service/student.service';

@Component({
  selector: 'app-recruit-student',
  templateUrl: './recruit-student.component.html',
  styleUrls: ['./recruit-student.component.css']
})
export class RecruitStudentComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  student: StudentWithUserId | undefined
  admissionForm!: AdmissionFormDetails;

  constructor(private studentService: StudentService, private formService: AdmissionFormService, 
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
        });
        
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

  apply(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.formService.recruit(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value,
        documentId: this.form.controls.documentId.value,
        programId: this.admissionForm.admission.program.id 
      }).subscribe(
        (response: StudentProgram) => {
          
        },
        (error: HttpErrorResponse) => {
          this.message = error.error.message;
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          else if(error.status === 403) {
            this.message = "You have no permission to add student!";
          }
          this.loginInvalid = true;
        }
      )
    }
  }

}
