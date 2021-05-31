import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { StudyGroupForPage } from 'src/app/entity/study-group-for-page';
import { CourseYearService } from 'src/app/service/course-year.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-study-groups',
  templateUrl: './show-study-groups.component.html',
  styleUrls: ['./show-study-groups.component.css']
})
export class ShowStudyGroupsComponent extends PageableGetAllChildrenComponent<StudyGroupForPage, CourseYearDetails> implements OnInit {
  parentId: number;
  parentName: string = '';
  parentDate: string = '';

  constructor(protected service: StudyGroupService, protected userService: UserService, 
    private parentService: CourseYearService,private dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, userService, router, route);
      this.parentId = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.checkAuthority("ROLE_COURSE_ADMIN");

    this.parentService.getById(this.id).subscribe(
      (response: CourseYearDetails) => {
        this.parentName = response.parent.name;  
        this.parentDate = new Date(response.startDate).getFullYear() + '/' +
        new Date(response.endDate).getFullYear();
        this.parentId = response.id;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
      }
    )
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete study group", 
      question: "Do you want to remove study group " + name + "?"
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
