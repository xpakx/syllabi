import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationToken } from '../../entity/authentication-token';
import { AuthenticationService } from '../../service/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;

  constructor(private authService: AuthenticationService, private fb: FormBuilder,
    private router: Router) { 
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {
  }

  public login(): void {
    if(!this.form.valid) {
      return;
    }

    this.authService.authenticate({
      username: this.form.controls.username.value,
      password: this.form.controls.password.value
    }).subscribe(
      (response: AuthenticationToken) => {
        localStorage.setItem("token", response.token);
        localStorage.setItem("user_id", response.id);
        this.router.navigate([""]);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
        this.loginInvalid = true;
      }
    )
  }

}
