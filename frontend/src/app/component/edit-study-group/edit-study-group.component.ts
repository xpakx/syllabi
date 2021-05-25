import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { StudyGroup } from 'src/app/entity/study-group';
import { TeacherSummary } from 'src/app/entity/teacher-summary';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalCoordinatorsChoiceComponent } from '../modal-coordinators-choice/modal-coordinators-choice.component';
import { ModalCourseTypeChoiceComponent } from '../modal-course-type-choice/modal-course-type-choice.component';

@Component({
  selector: 'app-edit-study-group',
  templateUrl: './edit-study-group.component.html',
  styleUrls: ['./edit-study-group.component.css']
})
export class EditStudyGroupComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  teachers: TeacherSummary[] = [];
  type!: CourseType;
  parentName: string = "";
  typeName: string = "Choose type";
  id: number;
  group!: StudyGroup;

  constructor(private groupService: StudyGroupService, private route: ActivatedRoute, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit(): void {
    this.groupService.getStudyGroupById(this.id).subscribe(
      (result: StudyGroup) => {
        this.group = result;
        this.form = this.fb.group({
          name: [this.group.name, Validators.maxLength(600)],
          description: [this.group.description, Validators.maxLength(600)],
          studentLimit: [this.group.studentLimit, Validators.required],
          ongoing: [this.group.ongoing]
        });
        this.teachers = this.group.teachers;
        this.type = this.group.type;
        this.typeName = this.group.type.name;
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

  editStudyGroup(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid && this.type) {
      this.groupService.editStudyGroup(id, {
        name: this.form.controls.name.value,
        description: this.form.controls.description.value,
        studentLimit : this.form.controls.studentLimit.value,
        ongoing : this.form.controls.ongoing.value,
        courseTypeId: this.type.id,
        teachers: this.teachers.map((p) => p.id)
      }).subscribe(
        (response: StudyGroup) => {
          
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
        }
      }
    );
  }

}
