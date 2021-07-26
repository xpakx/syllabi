import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Program } from 'src/app/entity/program';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { Semester } from 'src/app/entity/semester';
import { SemesterService } from 'src/app/service/semester.service';

@Component({
  selector: 'app-add-semester',
  templateUrl: './add-semester.component.html',
  styleUrls: ['./add-semester.component.css']
})
export class AddSemesterComponent implements OnInit {
  form: FormGroup;
  public requestInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  private id: number;
  parentName!: string;

  constructor(private service: SemesterService, private route: ActivatedRoute, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.form = this.fb.group({
      name: ['', Validators.required],
      number: ['', [Validators.required, Validators.pattern("^[0-9]*$")]]
    });
  }

  ngOnInit(): void {
    this.service.getParentById(this.id).subscribe(
      (response: ProgramSummary) => {
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

  addSemester(): void {
    if(this.form.valid) {
      this.service.addNew(this.id, {
        name: this.form.controls.name.value,
        number: this.form.controls.number.value
      }).subscribe(
        (response: Semester) => {
          this.router.navigate(["semesters/"+response.id]);
        },
        (error: HttpErrorResponse) => {
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          this.message = error.error.message;
          this.requestInvalid = true;
        }
      )
    } else {
      this.message = "Form invalid!";
      this.requestInvalid = true;
    }
  }

}
