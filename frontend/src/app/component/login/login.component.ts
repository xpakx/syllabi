import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLinkWithHref } from '@angular/router';
import { AuthenticationRequest } from '../../entity/authentication-request';
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

    let auth: AuthenticationRequest = new AuthenticationRequest(
      this.form.controls.username.value, 
      this.form.controls.password.value
    );
    this.authService.authenticate(auth).subscribe(
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
