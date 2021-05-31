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
  

  constructor(protected service: UserService, protected dialog: MatDialog, 
  protected router: Router, protected userService: UserService) {  
    super(service, userService, router, dialog);
    this.elemTypeName = "user";
  }

  ngOnInit(): void {
    this.getFirstPage();
    this.admin = true;
  }
}
