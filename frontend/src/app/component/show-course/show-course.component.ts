import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseDetails } from 'src/app/entity/course-details';
import { CourseService } from 'src/app/service/course.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-course',
  templateUrl: './show-course.component.html',
  styleUrls: ['./show-course.component.css']
})
export class ShowCourseComponent extends ShowComponent<CourseDetails> implements OnInit {

  constructor(protected courseService: CourseService, protected userService: UserService,
     protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {  
      super(courseService, userService, router, route, dialog);
      this.redir = 'courses/';
      this.deleteRedir = 'courses/';
      this.elemTypeName = "course";
     }

  ngOnInit(): void {
    this.getElem();
  }
}
