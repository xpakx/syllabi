import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;

  constructor(private userService: UserService, 
    private fb: FormBuilder, private router: Router, 
    private route: ActivatedRoute) { 
    this.form = this.fb.group({
      passwordOld: ['', Validators.required],
      password: ['', Validators.required],
      passwordRe: ['', Validators.required]
    });
  }

  ngOnInit(): void { }

  changePassword(): void {
    const id = localStorage.getItem("user_id");
    if(this.form.valid && id) {
      this.userService.changePassword(id, {
        passwordOld: this.form.controls.passwordOld.value,
        password: this.form.controls.password.value,
        passwordRe: this.form.controls.passwordRe.value
      }).subscribe(
        (response: User) => {
          this.router.navigate(['users/'+id]);
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
