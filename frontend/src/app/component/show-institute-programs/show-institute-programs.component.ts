import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { InstituteProgramsAdapterService } from 'src/app/service/institute-programs-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalProgramDeleteComponent } from '../modal-program-delete/modal-program-delete.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-institute-programs',
  templateUrl: './show-institute-programs.component.html',
  styleUrls: ['./show-institute-programs.component.css']
})
export class ShowInstituteProgramsComponent extends PageableGetAllChildrenComponent<ProgramForPage, Institute> implements OnInit {
  
  constructor(protected service: InstituteProgramsAdapterService, protected userService: UserService,
    private dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) {  
      super(service, userService, router, route);
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
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
