import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseSummary } from 'src/app/entity/course-summary';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { CourseLiteratureService } from 'src/app/service/course-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-all-course-literature',
  templateUrl: './show-all-course-literature.component.html',
  styleUrls: ['./show-all-course-literature.component.css']
})
export class ShowAllCourseLiteratureComponent extends PageableGetAllChildrenComponent<LiteratureForPage, CourseSummary> implements OnInit {

  constructor(protected service: CourseLiteratureService, protected userService: UserService,
    protected dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) { 
      super(service, userService, router, route, dialog);
      this.elemTypeName = "literature";
      this.parentTypeName = "course";
     }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
