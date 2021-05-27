import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseYearDetails } from 'src/app/entity/course-year-details';
import { StudyGroupForPage } from 'src/app/entity/study-group-for-page';
import { CourseYearService } from 'src/app/service/course-year.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalDeleteStudyGroupComponent } from '../modal-delete-study-group/modal-delete-study-group.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-study-groups',
  templateUrl: './show-study-groups.component.html',
  styleUrls: ['./show-study-groups.component.css']
})
export class ShowStudyGroupsComponent extends PageableGetAllChildrenComponent<StudyGroupForPage> implements OnInit {
  parentId: number;
  parentName: string = '';
  parentDate: string = '';

  constructor(protected service: StudyGroupService, private parentService: CourseYearService,private dialog: MatDialog, 
    protected route: ActivatedRoute, protected router: Router) { 
      super(service, router, route);
      this.parentId = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    this.getFirstPage();

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
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteStudyGroupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }
}
