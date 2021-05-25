import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Job } from 'src/app/entity/job';
import { TeacherService } from 'src/app/service/teacher.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-edit-teacher-job',
  templateUrl: './edit-teacher-job.component.html',
  styleUrls: ['./edit-teacher-job.component.css']
})
export class EditTeacherJobComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  job: Job | undefined;
  institute: number | undefined;
  instituteName: string = "Choose institute";

  constructor(private teacherService: TeacherService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) { 
    
  }

  ngOnInit(): void { 
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.teacherService.getTeacherJob(id).subscribe(
      (result: Job) => {
        this.job = result;
        this.form = this.fb.group({
          name: [this.job.name, Validators.required]
        });
        this.institute = this.job.institute.id;
        this.instituteName = this.job.institute.name;
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

  editJob(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid && this.institute) {
      this.teacherService.editTeacherJob(id, {
        name: this.form.controls.name.value,
        instituteId: this.institute
      }).subscribe(
        (response: Job) => {
          
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
