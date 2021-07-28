import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';

@Component({
  selector: 'app-edit-institute',
  templateUrl: './edit-institute.component.html',
  styleUrls: ['./edit-institute.component.css']
})
export class EditInstituteComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  parent: number | undefined;
  parentName: string = "Choose parent";
  institute: Institute | undefined;

  constructor(private instituteService: InstituteService, 
    private fb: FormBuilder, private dialog: MatDialog,
    private route: ActivatedRoute, private router: Router) { 
  }

  ngOnInit(): void {
    
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.instituteService.getById(id).subscribe(
      (result: Institute) => {
        this.institute = result;
        this.form = this.fb.group({
          name: [this.institute.name, Validators.required],
          code: [this.institute.code, Validators.required],
          url: [this.institute.url],
          phone: [this.institute.phone],
          address: [this.institute.address],
        });
        this.parent = this.institute.parent.id;
        this.parentName = this.institute.parent.name;
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

  editInstitute(): void {

    if(this.form.valid) {
      const id = Number(this.route.snapshot.paramMap.get('id'));
      this.instituteService.edit(id, {
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

  deleteParent(): void {
    this.parent = undefined;
    this.parentName = "Choose parent";
  }
}
