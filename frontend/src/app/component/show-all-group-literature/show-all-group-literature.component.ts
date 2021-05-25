import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { LiteratureForPage } from 'src/app/entity/literature-for-page';
import { Page } from 'src/app/entity/page';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { LiteratureService } from 'src/app/service/literature.service';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalDeleteGroupLiteratureComponent } from '../modal-delete-group-literature/modal-delete-group-literature.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-all-group-literature',
  templateUrl: './show-all-group-literature.component.html',
  styleUrls: ['./show-all-group-literature.component.css']
})
export class ShowAllGroupLiteratureComponent extends PageableComponent<LiteratureForPage> implements OnInit {
  group: StudyGroupSummary | undefined;

  constructor(private literatureService: LiteratureService, private groupService: StudyGroupService,
    private dialog: MatDialog, private route: ActivatedRoute, 
    private router: Router) { 
      super();
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getAllGroupLiterature(id).subscribe(
      (response: Page<LiteratureForPage>) => {
        this.printPage(response);
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

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.literatureService.getAllGroupLiteratureForPage(id, page).subscribe(
      (response: Page<LiteratureForPage>) => {
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

  delete(id: number, name: string, groupName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, courseName: groupName};
    const dialogRef = this.dialog.open(ModalDeleteGroupLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }

}
