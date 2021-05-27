import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Student } from 'src/app/entity/student';
import { StudentWithUserId } from 'src/app/entity/student-with-user-id';
import { StudentService } from 'src/app/service/student.service';

@Component({
  selector: 'app-edit-student',
  templateUrl: './edit-student.component.html',
  styleUrls: ['./edit-student.component.css']
})
export class EditStudentComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  student: StudentWithUserId | undefined

  constructor(private studentService: StudentService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) {   }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.studentService.getStudentByUserId(id).subscribe(
      (result: StudentWithUserId) => {
        this.student = result;
        this.form = this.fb.group({
          name: [this.student.name, Validators.required],
          surname: [this.student.surname, Validators.required]
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

  editStudent(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.studentService.edit(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value
      }).subscribe(
        (response: Student) => {
          
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
