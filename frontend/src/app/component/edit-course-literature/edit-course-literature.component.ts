import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Literature } from 'src/app/entity/literature';
import { LiteratureService } from 'src/app/service/literature.service';

@Component({
  selector: 'app-edit-course-literature',
  templateUrl: './edit-course-literature.component.html',
  styleUrls: ['./edit-course-literature.component.css']
})
export class EditCourseLiteratureComponent implements OnInit {
  form!: FormGroup;
  public loginInvalid: boolean = false;
  public message: string = '';
  private formSubmitAttempt: boolean = false;
  literature!: Literature;

  constructor(private literatureService: LiteratureService, private fb: FormBuilder, 
    private dialog: MatDialog, private router: Router,
    private route: ActivatedRoute) {  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getCourseLiteratureById(id).subscribe(
      (result: Literature) => {
        this.literature = result;
        this.form = this.fb.group({
          title: [this.literature.title, Validators.required],
          author: [this.literature.author, Validators.required],
          pages: [this.literature.pages],
          edition: [this.literature.edition],
          description: [this.literature.description, Validators.maxLength(600)],
          obligatory: [this.literature.obligatory]
        });
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

  editLiterature(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if(this.form.valid) {
      this.literatureService.editCourseLiterature(id, {
        'title': this.form.controls.title.value,
        'author': this.form.controls.author.value,
        'pages': this.form.controls.pages.value,
        'edition': this.form.controls.edition.value,
        'description': this.form.controls.description.value,
        'obligatory': this.form.controls.obligatory.value
      }).subscribe(
        (response: Literature) => {
          
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

}
