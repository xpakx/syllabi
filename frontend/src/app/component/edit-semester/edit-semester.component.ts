import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Semester } from 'src/app/entity/semester';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { SemesterService } from 'src/app/service/semester.service';

@Component({
  selector: 'app-edit-semester',
  templateUrl: './edit-semester.component.html',
  styleUrls: ['./edit-semester.component.css']
})
export class EditSemesterComponent implements OnInit {
  form!: FormGroup;
  public requestInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  private id: number;
  semester!: SemesterSummary;

  constructor(private service: SemesterService, private route: ActivatedRoute, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    this.service.getById(this.id).subscribe(
      (response: SemesterSummary) => {
        this.semester = response;
        this.form = this.fb.group({
          name: [this.semester.name, Validators.required],
          number: [this.semester.number, Validators.required]
        });
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  editSemester(): void {
    if(this.form.valid) {
      this.service.edit(this.id, {
        name: this.form.controls.name.value,
        number: this.form.controls.number.value
      }).subscribe(
        (response: Semester) => {
          
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
    }
  }

}
