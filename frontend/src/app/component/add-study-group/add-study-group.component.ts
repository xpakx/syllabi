import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { StudyGroup } from 'src/app/entity/study-group';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { CourseYearService } from 'src/app/service/course-year.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalCoordinatorsChoiceComponent } from '../modal-coordinators-choice/modal-coordinators-choice.component';
import { ModalCourseTypeChoiceComponent } from '../modal-course-type-choice/modal-course-type-choice.component';

@Component({
  selector: 'app-add-study-group',
  templateUrl: './add-study-group.component.html',
  styleUrls: ['./add-study-group.component.css']
})
export class AddStudyGroupComponent implements OnInit {
  form: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  id: number;
  teachers: TeacherSummary[] = [];
  type!: CourseType | undefined;
  parentName: string = "";
  typeName: string = "Choose type *";
  typeInvalid: boolean = false;

  constructor(private parentService: CourseYearService, private service: StudyGroupService,
    private route: ActivatedRoute, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
      this.form = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.maxLength(600)],
        studentLimit: ['', Validators.required],
        ongoing: ['']
      });
  }

  ngOnInit(): void {
    this.parentService.getById(this.id).subscribe(
      (response: CourseYearDetails) => {
        this.parentName = response.parent.name;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  addStudyGroup(): void {
    if(this.form.valid && this.type) {
      this.service.addNew(this.id, {
        description: this.form.controls.description.value,
        name: this.form.controls.name.value,
        studentLimit : this.form.controls.studentLimit.value,
        ongoing : this.form.controls.ongoing.value,
        courseTypeId: this.type.id,
        teachers: this.teachers.map((p) => p.id)
      }).subscribe(
        (response: StudyGroup) => {
          this.router.navigate(["groups/"+response.id]);          
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
    } else {
      this.message = "Form invalid!";
      if(!this.type) {
        this.typeInvalid = true;
      } 
      this.loginInvalid = true;
    }
  }

  addTeachers(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = this.teachers;
    const dialogRef = this.dialog.open(ModalCoordinatorsChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          
        }
      }
    );
  }

  addType(): void {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    const dialogRef = this.dialog.open(ModalCourseTypeChoiceComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        if(data) {
          this.type = {id: data.id, name: data.name};
          this.typeName = data.name;
          this.typeInvalid = false;
        } else {
          this.typeInvalid = true;
        }
      }
    );
  }
  
  deleteType(): void {
    this.type = undefined;
    this.typeName = "Choose type *";
    this.typeInvalid = true;
  }
}
