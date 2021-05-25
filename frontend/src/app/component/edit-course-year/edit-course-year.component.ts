import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYear } from 'src/app/entity/course-year';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { CourseYearService } from 'src/app/service/course-year.service';
import { ModalCoordinatorsChoiceComponent } from '../modal-coordinators-choice/modal-coordinators-choice.component';

@Component({
  selector: 'app-edit-course-year',
  templateUrl: './edit-course-year.component.html',
  styleUrls: ['./edit-course-year.component.css']
})
export class EditCourseYearComponent implements OnInit {
  form!: FormGroup;
  public error: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  id: number;

  coordinators: TeacherSummary[] = [];

  year: CourseYearDetails | undefined;

  constructor(private yearService: CourseYearService, private fb: FormBuilder, 
    private dialog: MatDialog, private route: ActivatedRoute,
    private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
     }

  ngOnInit(): void {
    
    this.yearService.getCourseYearById(this.id).subscribe(
      (result: CourseYearDetails) => {
        this.year = result;
        this.form = this.fb.group({
          description: [result.description, Validators.maxLength(600)],
          assessmentRules: [result.assessmentRules, Validators.maxLength(300)],
          commentary: [result.commentary, Validators.maxLength(300)],
          startDate: [result.startDate, Validators.required],
          endDate: [result.endDate, Validators.required],
        });
        this.coordinators = this.year.coordinatedBy;
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

  editCourseYear(): void {
    if(this.form.valid && this.id && this.year) {


      this.yearService.editCourseYear(this.id, {
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
          this.error = true;
        }
      )
    }
    else {alert("invalid");}
  }

  addCoordinators(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = this.coordinators;
    const dialogRef = this.dialog.open(ModalCoordinatorsChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.coordinators = data;
        }
      }
    );
  }

}
