import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from 'src/app/entity/teacher';
import { User } from 'src/app/entity/user';
import { TeacherService } from 'src/app/service/teacher.service';
import { UserService } from 'src/app/service/user.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-add-teacher',
  templateUrl: './add-teacher.component.html',
  styleUrls: ['./add-teacher.component.css']
})
export class AddTeacherComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  institute: number | undefined;
  instituteName: string = "Choose institute";
  name: string = "";

  constructor(private teacherService: TeacherService, private userService: UserService,
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      title: ['', Validators.required],
      phone: [''],
      email: [''],
      pbnId: [''],
      jobName: ['', Validators.required]
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

  addTeacher(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid && this.institute) {
      this.teacherService.addNewTeacher(id, {
        name: this.form.controls.name.value,
        surname: this.form.controls.surname.value,
        title: this.form.controls.title.value,
        phone: this.form.controls.phone.value,
        email: this.form.controls.email.value,
        pbnId: this.form.controls.pbnId.value,
        jobName: this.form.controls.jobName.value,
        instituteId: this.institute
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

  addInstitute(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    const dialogRef = this.dialog.open(ModalOrganizerChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.institute = data.id; this.instituteName = data.name;
        }
      }
    );
  }

}
