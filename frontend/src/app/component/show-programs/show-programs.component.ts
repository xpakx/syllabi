import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Page } from 'src/app/entity/page';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { ProgramService } from 'src/app/service/program.service';
import { UserService } from 'src/app/service/user.service';
import { ModalProgramDeleteComponent } from '../modal-program-delete/modal-program-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-programs',
  templateUrl: './show-programs.component.html',
  styleUrls: ['./show-programs.component.css']
})
export class ShowProgramsComponent extends PageableGetAllComponent<ProgramForPage> implements OnInit {
  
  constructor(protected service: ProgramService, private dialog: MatDialog,
    protected router: Router, protected userService: UserService) { 
      super(service, userService, router);
    }

  ngOnInit(): void {
    this.getFirstPage();
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
