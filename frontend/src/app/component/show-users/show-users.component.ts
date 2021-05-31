import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { PageableGetAllComponent } from '../pageable/pageable-get-all.component';

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent extends PageableGetAllComponent<User> implements OnInit {
  

  constructor(protected service: UserService,private dialog: MatDialog, 
  protected router: Router, protected userService: UserService) {  
    super(service, userService, router);
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.admin = true;
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete user", 
      question: "Do you want to remove user " + name + "?"
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
