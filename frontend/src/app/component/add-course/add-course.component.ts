import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseService } from '../../service/course.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CourseResponse } from 'src/app/entity/course-response';

@Component({
  selector: 'app-add-course',
  templateUrl: './add-course.component.html',
  styleUrls: ['./add-course.component.css']
})
export class AddCourseComponent implements OnInit {
  form: FormGroup;
  public invalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  institute: number | undefined;
  instituteName: string = "Choose institute";

  constructor(private courseService: CourseService, private fb: FormBuilder, 
    private dialog: MatDialog, private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      courseCode: ['', Validators.required],
      iscedCode: ['', Validators.required],
      erasmusCode: ['', Validators.required],
      ects: ['', [Validators.required, Validators.pattern("^[0-9]*$")]],
      language: ['', Validators.required],
      shortDescription: ['', Validators.maxLength(300)],
      description: ['', Validators.maxLength(600)],
      assessmentRules: ['', Validators.maxLength(300)],
      effects: ['', Validators.maxLength(300)],
      requirements: ['', Validators.maxLength(300)],
      facultative: [''],
      stationary: [''],
    });
  }

  ngOnInit(): void {
  }

  addCourse(): void {
    if(this.form.valid) {
      this.courseService.addNew({
        'name': this.form.controls.name.value,
        'courseCode': this.form.controls.courseCode.value,
        'iscedCode': this.form.controls.iscedCode.value,
        'erasmusCode': this.form.controls.erasmusCode.value,
        'ects': this.form.controls.ects.value,
        'language': this.form.controls.language.value,
        'shortDescription': this.form.controls.shortDescription.value,
        'description': this.form.controls.description.value,
        'assessmentRules': this.form.controls.assessmentRules.value,
        'effects': this.form.controls.effects.value,
        'requirements': this.form.controls.requirements.value,
        'facultative': this.form.controls.facultative.value,
        'stationary': this.form.controls.stationary.value,
        'organizerId' : this.institute
      }).subscribe(
        (response: CourseResponse) => {
          this.router.navigate(['courses/'+response.id]);
        },
        (error: HttpErrorResponse) => {
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          this.message = error.error.message;
          this.invalid = true;
        }
      )
    } else {
      this.invalid = true;
      this.message = "Invalid form"; 
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
    this.instituteName = "Choose institute";
  }
 
}
