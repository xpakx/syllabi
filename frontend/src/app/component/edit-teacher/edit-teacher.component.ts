import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from 'src/app/entity/teacher';
import { TeacherService } from 'src/app/service/teacher.service';

@Component({
  selector: 'app-edit-teacher',
  templateUrl: './edit-teacher.component.html',
  styleUrls: ['./edit-teacher.component.css']
})
export class EditTeacherComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  teacher: Teacher | undefined;

  constructor(private teacherService: TeacherService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) { 
    
  }

  ngOnInit(): void { 
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.teacherService.getTeacherByUserId(id).subscribe(
      (result: Teacher) => {
        this.teacher = result;
        this.form = this.fb.group({
          name: [this.teacher.name, Validators.required],
          surname: [this.teacher.surname, Validators.required],
          title: [this.teacher.title, Validators.required],
          phone: [this.teacher.phone],
          email: [this.teacher.email],
          pbnId: [this.teacher.pbnId]
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

  editTeacher(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.teacherService.editTeacher(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value,
        title: this.form.controls.title.value,
        phone: this.form.controls.phone.value,
        email: this.form.controls.email.value,
        pbnId: this.form.controls.pbnId.value
      }).subscribe(
        (response: Teacher) => {
          
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
