import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/entity/user';
import { UserService } from 'src/app/service/user.service';
import { ModalUserDeleteComponent } from '../modal-user-delete/modal-user-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-user',
  templateUrl: './show-user.component.html',
  styleUrls: ['./show-user.component.css']
})
export class ShowUserComponent extends ShowComponent<User> implements OnInit {

  constructor(protected userService: UserService, protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) { 
        super(userService, userService, router, route);
    }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalUserDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }

}
