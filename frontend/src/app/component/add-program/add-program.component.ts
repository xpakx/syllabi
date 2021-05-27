import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Program } from 'src/app/entity/program';
import { ProgramService } from 'src/app/service/program.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-add-program',
  templateUrl: './add-program.component.html',
  styleUrls: ['./add-program.component.css']
})
export class AddProgramComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  institute: number | undefined;
  instituteName: string = "Choose organizer";

  constructor(private programService: ProgramService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.maxLength(600)],
      ongoing: ['']
    });
  }

  ngOnInit(): void { }

  addProgram(): void {

    if(this.form.valid && this.institute) {
      this.programService.addNew({
        description: this.form.controls.description.value,
        name: this.form.controls.name.value,
        organizerId: this.institute,
        coursesId: []
      }).subscribe(
        (response: Program) => {
          
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

  addInstitutes(): void {
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
