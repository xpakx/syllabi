import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-add-user-role',
  templateUrl: './add-user-role.component.html',
  styleUrls: ['./add-user-role.component.css']
})
export class AddUserRoleComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;

  constructor(private userService: UserService, 
    private fb: FormBuilder, private router: Router, 
    private route: ActivatedRoute) { 
    this.form = this.fb.group({
      role: ['', Validators.required]
    });
  }

  ngOnInit(): void { }

  addRole(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.userService.addRole(id, {
        role: this.form.controls.role.value
      }).subscribe(
        (response: User) => {
          
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
