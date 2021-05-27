import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { CourseYear } from 'src/app/entity/course-year';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { CourseYearService } from 'src/app/service/course-year.service';
import { CourseService } from 'src/app/service/course.service';
import { ModalCoordinatorsChoiceComponent } from '../modal-coordinators-choice/modal-coordinators-choice.component';

@Component({
  selector: 'app-add-course-year',
  templateUrl: './add-course-year.component.html',
  styleUrls: ['./add-course-year.component.css']
})
export class AddCourseYearComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  coordinators: TeacherSummary[] = [];
  parentName: string = "";

  constructor(private courseService: CourseService, private yearService: CourseYearService,
    private route: ActivatedRoute, private fb: FormBuilder, 
    private dialog: MatDialog, private router: Router) { 
    this.form = this.fb.group({
      description: ['', Validators.maxLength(600)],
      assessmentRules: ['', Validators.maxLength(300)],
      commentary: ['', Validators.maxLength(300)],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.courseService.getByIdMin(id).subscribe(
      (response: CourseSummary) => {
        this.parentName = response.name;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  addCourseYear(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.yearService.addNew(id, {
        description: this.form.controls.description.value,
        assessmentRules : this.form.controls.assessmentRules.value,
        commentary : this.form.controls.commentary.value,
        startDate : this.form.controls.startDate.value,
        endDate : this.form.controls.endDate.value,
        coordinators: this.coordinators.map((p) => p.id)
      }).subscribe(
        (response: CourseYear) => {
          this.router.navigate(["years/"+response.id]);
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

  addCoordinators(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = this.coordinators;
    const dialogRef = this.dialog.open(ModalCoordinatorsChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          
        }
      }
    );
  }
}
