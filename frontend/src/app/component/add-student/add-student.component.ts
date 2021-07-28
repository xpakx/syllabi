import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Student } from 'src/app/entity/student';
import { User } from 'src/app/entity/user';
import { StudentService } from 'src/app/service/student.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.css']
})
export class AddStudentComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  name: string = "";

  constructor(private studentService: StudentService, private userService: UserService,
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required]
    });
  }

  ngOnInit(): void { 
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.userService.getById(id).subscribe(
      (response: User) => {
        this.name = response.username;
      },
      (error: HttpErrorResponse) => {}
    )
  }

  addStudent(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.studentService.addNew(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value
      }).subscribe(
        (response: Student) => {
          this.router.navigate(['users/'+id+'/student']);
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
    } else {
      this.loginInvalid = true;
      this.message = "Form invalid!"
    }
  }
}
