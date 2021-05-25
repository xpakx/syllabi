import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseDetails } from 'src/app/entity/course-details';
import { CourseService } from 'src/app/service/course.service';
import { ModalDeleteCourseComponent } from '../modal-delete-course/modal-delete-course.component';

@Component({
  selector: 'app-show-course',
  templateUrl: './show-course.component.html',
  styleUrls: ['./show-course.component.css']
})
export class ShowCourseComponent implements OnInit {
  course: CourseDetails | undefined;
  message: string = '';
  id: number;

  constructor(private courseService: CourseService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) {  
      this.id = Number(this.route.snapshot.paramMap.get('id'));
     }

  ngOnInit(): void {
    this.courseService.getCourseById(this.id).subscribe(
      (result: CourseDetails) => {
        this.course = result;
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

  loadCourse(id: number): void {
    this.router.navigate(['courses/'+id]);
    this.courseService.getCourseById(id).subscribe(
      (result: CourseDetails) => {
        this.course = result;
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

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteCourseComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
        if(data) {
          this.router.navigate(['courses']);
        }
      }
    );
  }

}
