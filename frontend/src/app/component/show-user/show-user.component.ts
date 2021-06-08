import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-user',
  templateUrl: './show-user.component.html',
  styleUrls: ['./show-user.component.css']
})
export class ShowUserComponent extends ShowComponent<User> implements OnInit {

  constructor(protected userService: UserService, protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) { 
        super(userService, userService, router, route, dialog);
        this.elemTypeName = "user";
    }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(['users/']);
  }
}
