import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteForPage } from 'src/app/entity/institute-for-page';
import { InstituteChildrenAdapterService } from 'src/app/service/institute-children-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteInstituteComponent } from '../modal-delete-institute/modal-delete-institute.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-institute-children',
  templateUrl: './show-institute-children.component.html',
  styleUrls: ['./show-institute-children.component.css']
})
export class ShowInstituteChildrenComponent extends PageableGetAllChildrenComponent<InstituteForPage, Institute> implements OnInit {

  constructor(protected service: InstituteChildrenAdapterService, protected userService: UserService,
    private dialog: MatDialog,
    protected router: Router, protected route: ActivatedRoute) { 
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
    const dialogRef = this.dialog.open(ModalDeleteInstituteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
