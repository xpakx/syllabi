import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseForPage } from 'src/app/entity/course-for-page';
import { Program } from 'src/app/entity/program';
import { ProgramCoursesAdapterService } from 'src/app/service/program-courses-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-program-courses',
  templateUrl: './show-program-courses.component.html',
  styleUrls: ['./show-program-courses.component.css']
})
export class ShowProgramCoursesComponent extends PageableGetAllChildrenComponent<CourseForPage, Program> implements OnInit {
  
  constructor(protected service: ProgramCoursesAdapterService, protected userService: UserService,
    private dialog: MatDialog,
  protected route: ActivatedRoute, protected router: Router) {  
    super(service, userService, router, route);
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
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
    this.service.delete(id).subscribe(
      (response) => {
        this.getPage(this.page);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
