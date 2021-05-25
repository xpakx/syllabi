import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { CourseYearService } from 'src/app/service/course-year.service';
import { ModalDeleteCourseYearComponent } from '../modal-delete-course-year/modal-delete-course-year.component';

@Component({
  selector: 'app-show-course-year',
  templateUrl: './show-course-year.component.html',
  styleUrls: ['./show-course-year.component.css']
})
export class ShowCourseYearComponent implements OnInit {
  year: CourseYearDetails | undefined;
  message: string = '';
  id: number;

  constructor(private yearService: CourseYearService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    this.yearService.getCourseYearById(this.id).subscribe(
      (result: CourseYearDetails) => {
        this.year = result;
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

  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id};
    const dialogRef = this.dialog.open(ModalDeleteCourseYearComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
        if(data) {
         this.router.navigate(['courses/'+this.year?.parent.id+'/years']);
       }
      }
    );
  }

}
