import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-add-institute',
  templateUrl: './add-institute.component.html',
  styleUrls: ['./add-institute.component.css']
})
export class AddInstituteComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  parent: number | undefined;
  parentName: string = "Choose parent";

  constructor(private instituteService: InstituteService, 
    private fb: FormBuilder, private dialog: MatDialog, 
    private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      code: ['', Validators.required],
      url: [''],
      phone: [''],
      address: [''],
    });
  }

  ngOnInit(): void { }

  addInstitute(): void {

    if(this.form.valid) {
      this.instituteService.addNew({
        name: this.form.controls.name.value,
        code: this.form.controls.code.value,
        url: this.form.controls.url.value,
        phone: this.form.controls.phone.value,
        address: this.form.controls.address.value,
        parentId: this.parent
      }).subscribe(
        (response: Institute) => {
          
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

  addParent(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    const dialogRef = this.dialog.open(ModalOrganizerChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.parent = data.id; this.parentName = data.name;
        }
      }
    );
  }
}
