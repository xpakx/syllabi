import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteService } from 'src/app/service/institute.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteInstituteComponent } from '../modal-delete-institute/modal-delete-institute.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-institute',
  templateUrl: './show-institute.component.html',
  styleUrls: ['./show-institute.component.css']
})
export class ShowInstituteComponent extends ShowComponent<Institute> implements OnInit {
  institute: Institute | undefined;

  constructor(protected instituteService: InstituteService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) { 
      super(instituteService, userService, router, route);
    }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteInstituteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }

}
