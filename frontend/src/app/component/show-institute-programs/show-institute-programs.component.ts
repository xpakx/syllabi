import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { Page } from 'src/app/entity/page';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { InstituteCoursesAdapterService } from 'src/app/service/institute-courses-adapter.service';
import { InstituteProgramsAdapterService } from 'src/app/service/institute-programs-adapter.service';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalProgramDeleteComponent } from '../modal-program-delete/modal-program-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-institute-programs',
  templateUrl: './show-institute-programs.component.html',
  styleUrls: ['./show-institute-programs.component.css']
})
export class ShowInstituteProgramsComponent extends PageableGetAllChildrenComponent<ProgramForPage, Institute> implements OnInit {
  institute: Institute | undefined;
  
  constructor(protected service: InstituteProgramsAdapterService, private dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) {  
      super(service, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();

    this.service.getParentById(this.id).subscribe(
      (result: Institute) => {
        this.institute = result;
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
    const dialogRef = this.dialog.open(ModalProgramDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
