import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Course } from 'src/app/entity/course';
import { CourseDetails } from 'src/app/entity/course-details';
import { CourseResponse } from 'src/app/entity/course-response';
import { CourseSummary } from 'src/app/entity/course-summary';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { CourseService } from 'src/app/service/course.service';
import { ModalOrganizerChoiceComponent } from '../modal-organizer-choice/modal-organizer-choice.component';
import { ModalPrerequisiteChoiceComponent } from '../modal-prerequisite-choice/modal-prerequisite-choice.component';
import { ModalProgramChoiceComponent } from '../modal-program-choice/modal-program-choice.component';

@Component({
  selector: 'app-edit-course',
  templateUrl: './edit-course.component.html',
  styleUrls: ['./edit-course.component.css']
})
export class EditCourseComponent implements OnInit {

  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  id: number | undefined;

  institute: number | undefined;
  instituteName: string = "Choose institute";

  prerequisites: CourseSummary[] = [];
  semesters: SemesterSummary[] = [];

  course: CourseDetails | undefined;

  constructor(private courseService: CourseService, private fb: FormBuilder, 
    private dialog: MatDialog, private route: ActivatedRoute,
    private router: Router) {  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.courseService.getById(this.id).subscribe(
      (result: CourseDetails) => {
        this.course = result;
        this.form = this.fb.group({
          name: [this.course.name, Validators.required],
          courseCode: [this.course.courseCode, Validators.required],
          iscedCode: [this.course.iscedCode, Validators.required],
          erasmusCode: [this.course.erasmusCode, Validators.required],
          ects: [this.course.ects, Validators.required],
          language: [this.course.language, Validators.required],
          shortDescription: [this.course.shortDescription, Validators.maxLength(300)],
          description: [this.course.description, Validators.maxLength(600)],
          assessmentRules: [this.course.assessmentRules, Validators.maxLength(300)],
          effects: [this.course.effects, Validators.maxLength(300)],
          requirements: [this.course.requirements, Validators.maxLength(300)],
          facultative: [this.course.facultative],
          stationary: [this.course.stationary],
        });
        this.prerequisites = this.course.prerequisites;
        this.semesters = this.course.semesters;
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

  editCourse(): void {
    if(this.form.valid && this.id) {

      let prereq = this.prerequisites ? this.prerequisites.map((p) => p.id) : undefined;
      let prog = this.semesters ? this.semesters.map((p) => p.id) : undefined;

      this.courseService.edit(this.id, {
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
        'organizerId' : this.institute,
        prerequisites: prereq,
        semesters: prog
      }).subscribe(
        (response: CourseResponse) => {
          this.router.navigate(["courses/"+response.id]);
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
    else {alert("invalid");}
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

  addPrograms(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = this.semesters;
    const dialogRef = this.dialog.open(ModalProgramChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.semesters = data;
        }
      }
    );
  }

  addPrerequisites(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = this.prerequisites;
    const dialogRef = this.dialog.open(ModalPrerequisiteChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.prerequisites = data;
        }
      }
    );
  }

}
