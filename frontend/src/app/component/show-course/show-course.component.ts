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
    private dialog: MatDialog, protected router: Router) {  
      super(courseService, userService, router, route);
      this.redir = 'courses/';
     }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete course", 
      question: "Do you want to remove course " + name + "?"
    };
    const dialogRef = this.dialog.open(ModalDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
            this.deleteElem(id);
          }
      }
    );
  }

  deleteElem(id: number) {
    this.courseService.delete(id).subscribe(
      (response) => {
        this.router.navigate(['courses']);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }

}
