import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { CourseTypeService } from 'src/app/service/course-type.service';

@Component({
  selector: 'app-add-course-type',
  templateUrl: './add-course-type.component.html',
  styleUrls: ['./add-course-type.component.css']
})
export class AddCourseTypeComponent implements OnInit {
  form: FormGroup;
  public requestInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;

  constructor(private typeService: CourseTypeService, private route: ActivatedRoute, 
    private fb: FormBuilder, private dialog: MatDialog,
    private router: Router) { 
    this.form = this.fb.group({
      name: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  addStudyGroup(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.typeService.addNew({
        name: this.form.controls.name.value
      }).subscribe(
        (response: CourseType) => {
          
        },
        (error: HttpErrorResponse) => {
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          this.message = error.error.message;
          this.requestInvalid = true;
        }
      )
    }
  }


}
