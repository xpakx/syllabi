import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { CourseTypeService } from 'src/app/service/course-type.service';

@Component({
  selector: 'app-edit-course-type',
  templateUrl: './edit-course-type.component.html',
  styleUrls: ['./edit-course-type.component.css']
})
export class EditCourseTypeComponent implements OnInit {
  form!: FormGroup;
  public requestInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  type!: CourseType;

  constructor(private typeService: CourseTypeService, private route: ActivatedRoute, 
    private fb: FormBuilder, private router: Router) { 
    
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.typeService.getById(id).subscribe(
      (result: CourseType) => {
        this.type = result;
        this.form = this.fb.group({
          name: [this.type.name, Validators.maxLength(600)],
        });
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        } else if(error.status === 404) {
          this.router.navigate(['404']);
        }
        this.message = error.error.message;
      }
    );
  }

  editCourseType(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.typeService.edit(id, {
        name: this.form.controls.name.value
      }).subscribe(
        (response: CourseType) => {
          this.router.navigate(['/types']);
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
