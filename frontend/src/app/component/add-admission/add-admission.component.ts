import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Admission } from 'src/app/entity/admission';
import { AdmissionWeight } from 'src/app/entity/admission-weight';
import { Program } from 'src/app/entity/program';
import { AdmissionService } from 'src/app/service/admission.service';
import { ProgramService } from 'src/app/service/program.service';
import { ModalAddWeightComponent } from '../modal-add-weight/modal-add-weight.component';

@Component({
  selector: 'app-add-admission',
  templateUrl: './add-admission.component.html',
  styleUrls: ['./add-admission.component.css']
})
export class AddAdmissionComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  weights: FormGroup[] = [];
  parentName: string = "";

  constructor(private programService: ProgramService, private service: AdmissionService,
    private route: ActivatedRoute, private fb: FormBuilder, 
    private dialog: MatDialog, private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      studentLimit: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programService.getById(id).subscribe(
      (response: Program) => {
        this.parentName = response.name;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  addAdmission(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    let weights_adm: AdmissionWeight[] = [];
    let valid_flag = true;

    for(let weight of this.weights) {
      if(weight.valid) {
        weights_adm.push({name: weight.controls.name.value,
        weight: weight.controls.weight.value})
      }
      else {
        valid_flag = false;
      }
    }

    if(this.form.valid && valid_flag) {
      this.service.addNew(id, {
        name: this.form.controls.name.value,
        studentLimit : this.form.controls.studentLimit.value,
        startDate : this.form.controls.startDate.value,
        endDate : this.form.controls.endDate.value,
        weights: weights_adm
      }).subscribe(
        (response: Admission) => {
          this.router.navigate(["admissions/"+response.id]);
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
    } else {
      this.message = "Form invalid!";
      this.loginInvalid = true;
    }
  }

  addWeight(): void {
    let tempForm: FormGroup = this.fb.group({
      name: ['', Validators.required],
      weight: ['', [Validators.required, Validators.pattern("^[0-9]*$")]]
    });
    this.weights.push(tempForm);
  }

  deleteWeight(i: number): void {
    this.weights.splice(i, 1);
  }
}
