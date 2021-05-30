import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { InstituteForPage } from 'src/app/entity/institute-for-page';
import { Page } from 'src/app/entity/page';
import { InstituteService } from 'src/app/service/institute.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteInstituteComponent } from '../modal-delete-institute/modal-delete-institute.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-institutes',
  templateUrl: './show-institutes.component.html',
  styleUrls: ['./show-institutes.component.css']
})
export class ShowInstitutesComponent extends PageableGetAllComponent<InstituteForPage> implements OnInit {
  
  constructor(protected service: InstituteService, private dialog: MatDialog,
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
    const dialogRef = this.dialog.open(ModalDeleteInstituteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }
}
