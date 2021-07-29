import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../../service/authentication.service';
import { AuthenticationToken } from '../../entity/authentication-token';
import { RegistrationRequest } from '../../entity/registration-request';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: FormGroup;
  public registerInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;

  constructor(private authService: AuthenticationService, private fb: FormBuilder, private router: Router) { 
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      passwordRe: ['', Validators.required],
    });
  }

  ngOnInit(): void {
  }

  public register(): void {
    if(!this.form.valid) {
      return;
    }

    this.authService.register({
      username: this.form.controls.username.value, 
      password: this.form.controls.password.value, 
      passwordRe: this.form.controls.passwordRe.value
    }).subscribe(
      (response: AuthenticationToken) => {
        localStorage.setItem("token", response.token);
        localStorage.setItem("user_id", response.id);
        this.router.navigate([""]);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
        this.registerInvalid = true;
      }
    )
  }

}
