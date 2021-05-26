import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { Page } from 'src/app/entity/page';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { ModalDeleteCourseTypeComponent } from '../modal-delete-course-type/modal-delete-course-type.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-course-types',
  templateUrl: './show-course-types.component.html',
  styleUrls: ['./show-course-types.component.css']
})
export class ShowCourseTypesComponent extends PageableComponent<CourseType> implements OnInit {
  
  constructor(private typeService: CourseTypeService, private dialog: MatDialog,
    private router: Router) {
      super();
    }

  ngOnInit(): void {
    this.typeService.getAll().subscribe(
      (response: Page<CourseType>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    )
  }

  getPage(page: number): void {
    this.typeService.getAllForPage(page).subscribe(
      (response: Page<CourseType>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    )
  }
  
  delete(id: number) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteCourseTypeComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
