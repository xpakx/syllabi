import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseType } from 'src/app/entity/course-type';
import { Page } from 'src/app/entity/page';
import { CourseTypeService } from 'src/app/service/course-type.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteCourseTypeComponent } from '../modal-delete-course-type/modal-delete-course-type.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-course-types',
  templateUrl: './show-course-types.component.html',
  styleUrls: ['./show-course-types.component.css']
})
export class ShowCourseTypesComponent extends PageableGetAllComponent<CourseType> implements OnInit {
  
  constructor(protected service: CourseTypeService, private dialog: MatDialog,
    protected router: Router, protected userService: UserService) {
      super(service, userService, router);
    }

  ngOnInit(): void {
    this.getFirstPage();
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
