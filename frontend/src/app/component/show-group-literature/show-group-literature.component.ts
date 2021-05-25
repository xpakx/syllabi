import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Literature } from 'src/app/entity/literature';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { LiteratureService } from 'src/app/service/literature.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalDeleteGroupLiteratureComponent } from '../modal-delete-group-literature/modal-delete-group-literature.component';

@Component({
  selector: 'app-show-group-literature',
  templateUrl: './show-group-literature.component.html',
  styleUrls: ['./show-group-literature.component.css']
})
export class ShowGroupLiteratureComponent implements OnInit {
  literature: Literature | undefined;
  message: string = '';
  group: StudyGroupSummary | undefined;

  constructor(private literatureService: LiteratureService, private groupService: StudyGroupService,
    private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getGroupLiteratureById(id).subscribe(
      (result: Literature) => {
        this.literature = result;
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    );


    this.groupService.getStudyGroupByIdMin(id).subscribe(
      (result: StudyGroupSummary) => {
        this.group = result;
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
    this.literatureService.getCourseLiteratureById(id).subscribe(
      (result: Literature) => {
        this.literature = result;
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

  delete(id: number, name: string, groupName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, courseName: groupName};
    const dialogRef = this.dialog.open(ModalDeleteGroupLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }

}
