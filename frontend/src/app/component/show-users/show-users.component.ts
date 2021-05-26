import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Page } from 'src/app/entity/page';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';
import { ModalUserDeleteComponent } from '../modal-user-delete/modal-user-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent extends PageableGetAllComponent<User> implements OnInit {
  

  constructor(protected service: UserService,private dialog: MatDialog, 
  protected router: Router) {  
    super(service, router);
  }

  ngOnInit(): void {
    this.getFirstPage();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalUserDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }

}
