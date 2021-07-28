import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Program } from 'src/app/entity/program';
import { ProgramService } from 'src/app/service/program.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-edit-program',
  templateUrl: './edit-program.component.html',
  styleUrls: ['./edit-program.component.css']
})
export class EditProgramComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  institute: number | undefined;
  instituteName: string = "Choose organizer *";

  program: Program | undefined;

  constructor(private programService: ProgramService, 
    private fb: FormBuilder, private dialog: MatDialog, 
    private route: ActivatedRoute, private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.maxLength(600)],
      ongoing: ['']
    });
  }

  ngOnInit(): void { 
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programService.getById(id).subscribe(
      (result: Program) => {
        this.program = result;
        this.form = this.fb.group({
          name: [this.program.name, Validators.maxLength(600)],
          description: [this.program.description, Validators.maxLength(600)]
        });
        this.institute = this.program.organizer.id;
        this.instituteName = this.program.organizer.name;
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

  addProgram(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if(this.form.valid && this.institute) {
      this.programService.edit(id, {
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

  deleteInstitute(): void {
    this.institute = undefined;
    this.instituteName = "Choose organizer *";
  }
}
